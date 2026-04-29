package com.wanghui.shiyue.agent.handler;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.CosineSimilarity;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Component
public class VectorHandler {

    private static final String QWEN_RERANK_URL = "https://dashscope.aliyuncs.com/api/v1/services/rerank/text-rerank/text-rerank";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Resource
    private EmbeddingStore<TextSegment> milvusEmbeddingStore;

    @Resource
    private EmbeddingModel embeddingModel;

    @Value("${langchain4j.community.dashscope.chat-model.api-key:}")
    private String dashscopeApiKey;

    /**
     * 构建向量
     * @param text
     */

    public void build(String text){
        // 1. 准备文档
        Document document = Document.from(text);

        // 2. 文档切分
        DocumentSplitter splitter = DocumentSplitters.recursive(500, 100);
        var segments = splitter.split(document);

        // 3. 向量化并存入 Milvus
        for (TextSegment segment : segments) {
            Embedding embedding = embeddingModel.embed(segment).content();
            milvusEmbeddingStore.add(embedding, segment);
        }
        System.out.println("✅ 文本已存入 Milvus");
    }

    /**
     * 按会话ID持久化长期记忆到向量库。
     */
    public void storeSessionMemory(String sessionId, String memoryText) {
        if (sessionId == null || sessionId.isBlank() || memoryText == null || memoryText.isBlank()) {
            return;
        }

        Document document = Document.from(memoryText);
        DocumentSplitter splitter = DocumentSplitters.recursive(500, 100);
        List<TextSegment> segments = splitter.split(document);
        for (TextSegment segment : segments) {
            segment.metadata().put("scalar", sessionId);
            segment.metadata().put("memoryType", "summary");
            Embedding embedding = embeddingModel.embed(segment).content();
            milvusEmbeddingStore.add(embedding, segment);
        }
    }

    /**
     * 按会话ID召回长期记忆文本。
     */
    public List<String> searchSessionMemories(String sessionId, String query, int maxResults) {
        if (sessionId == null || sessionId.isBlank() || query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        int topK = Math.max(1, maxResults);
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        Filter filter = metadataKey("scalar").isEqualTo(sessionId);
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .filter(filter)
                .build();
        EmbeddingSearchResult<TextSegment> result = milvusEmbeddingStore.search(request);
        List<EmbeddingMatch<TextSegment>> matches = result.matches();
        if (matches == null || matches.isEmpty()) {
            return Collections.emptyList();
        }
        return matches.stream()
                .map(EmbeddingMatch::embedded)
                .map(TextSegment::text)
                .collect(Collectors.toList());
    }

    /**
     * 检索向量
     * @param query
     * @return {@link EmbeddingMatch }<{@link TextSegment }>
     */

    public EmbeddingMatch<TextSegment> search(String query){

        Embedding queryEmbedding = embeddingModel.embed(query).content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(1)
                .build();
        EmbeddingSearchResult<TextSegment> results = milvusEmbeddingStore.search(embeddingSearchRequest);
        EmbeddingMatch<TextSegment> textSegmentEmbeddingMatch = results.matches().get(0);
        return textSegmentEmbeddingMatch;
    }

    /**
     * 检索向量
     * @param query
     * @return {@link EmbeddingMatch }<{@link TextSegment }>
     */

    public EmbeddingMatch<TextSegment> search(String query, String scalar){

        Embedding queryEmbedding = embeddingModel.embed(query).content();
        //启用标量过滤
        Filter filter = metadataKey("scalar").isEqualTo(scalar);

        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .filter(filter)
                .build();
        EmbeddingSearchResult<TextSegment> results = milvusEmbeddingStore.search(embeddingSearchRequest);
        List<EmbeddingMatch<TextSegment>> matches = results.matches();
        if (matches == null || matches.isEmpty()) {
            return null;
        }

        EmbeddingMatch<TextSegment> reRankMatch = reRankMatches(query, matches);
        return reRankMatch != null ? reRankMatch : matches.get(0);
    }

    private EmbeddingMatch<TextSegment> reRankMatches(String query, List<EmbeddingMatch<TextSegment>> matches) {
        if (dashscopeApiKey == null || dashscopeApiKey.isBlank() || matches.size() == 1) {
            return matches.get(0);
        }

        List<String> documents = matches.stream()
                .map(EmbeddingMatch::embedded)
                .map(TextSegment::text)
                .collect(Collectors.toList());

        try {
            JSONObject input = new JSONObject();
            input.put("query", query);
            input.put("documents", documents);

            JSONObject parameters = new JSONObject();
            parameters.put("top_n", 1);
            parameters.put("return_documents", false);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "qwen3-rerank");
            requestBody.put("input", input);
            requestBody.put("parameters", parameters);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(QWEN_RERANK_URL))
                    .timeout(Duration.ofSeconds(20))
                    .header("Authorization", "Bearer " + dashscopeApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJSONString()))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return matches.get(0);
            }

            JSONObject responseBody = JSONObject.parseObject(response.body());
            JSONObject output = responseBody.getJSONObject("output");
            if (output == null) {
                return matches.get(0);
            }

            JSONArray resultArray = output.getJSONArray("results");
            if (resultArray == null || resultArray.isEmpty()) {
                return matches.get(0);
            }

            Integer bestIndex = resultArray.getJSONObject(0).getInteger("index");
            if (bestIndex == null || bestIndex < 0 || bestIndex >= matches.size()) {
                return matches.get(0);
            }
            return matches.get(bestIndex);
        } catch (Exception e) {
            return matches.get(0);
        }
    }

    /**
     * 根据问题召回文本，并与标准答案做语义相似度评分,使用余弦相似度进行判断
     * @param query 检索问题
     * @param standardAnswer 标准答案
     * @return 评分结果，范围一般为 0~1
     */
    public double recallScore(String query, String standardAnswer){
        EmbeddingMatch<TextSegment> match = search(query);
        if (match == null || match.embedded() == null || match.embedded().text() == null) {
            return 0D;
        }

        String recallText = match.embedded().text();
        Embedding standardEmbedding = embeddingModel.embed(standardAnswer).content();
        Embedding recallEmbedding = embeddingModel.embed(recallText).content();
        return CosineSimilarity.between(standardEmbedding, recallEmbedding);
    }
}