package com.wanghui.shiyue.agent.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {
    // ====================== 你的 Milvus 配置 ======================
    private static final String MILVUS_HOST = "124.222.67.25";
    private static final int MILVUS_PORT = 19530;
    private static final String MILVUS_COLLECTION = "langchain_collection"; // 集合名
    private static final int DIMENSION = 1536; // text-embedding-3-small 维度
    @Bean
    public EmbeddingStore<TextSegment> milvusEmbeddingStore() {
        return MilvusEmbeddingStore.builder()
                .host(MILVUS_HOST)
                .port(MILVUS_PORT)
                .collectionName(MILVUS_COLLECTION)
                .dimension(DIMENSION)
                .build();
    }

    @Bean
    public EmbeddingModel createEmbeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey("sk-0aba85d1bf224988ae2f34ae023293b7")
                .modelName("text-embedding-v4")
                .dimensions(DIMENSION)
                .build();
    }

}