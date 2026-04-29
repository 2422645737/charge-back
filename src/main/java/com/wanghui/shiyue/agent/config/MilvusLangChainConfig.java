package com.wanghui.shiyue.agent.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import static dev.langchain4j.model.openai.OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;

import java.util.List;

public class MilvusLangChainConfig {
    // ====================== 你的 Milvus 配置 ======================
    private static final String MILVUS_HOST = "124.222.67.25";
    private static final int MILVUS_PORT = 19530;
    private static final String MILVUS_COLLECTION = "langchain_collection"; // 集合名
    private static final int DIMENSION = 1536; // text-embedding-3-small 维度

    /**
     * 1. 创建 Milvus 向量存储实例（核心！）
     */
    public static EmbeddingStore<TextSegment> createMilvusStore() {
        return MilvusEmbeddingStore.builder()
                .host(MILVUS_HOST)
                .port(MILVUS_PORT)
                .collectionName(MILVUS_COLLECTION)
                .dimension(DIMENSION)
                .build();
    }

    /**
     * 2. 创建嵌入模型（OpenAI 示例）
     */
    public static EmbeddingModel createEmbeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey("sk-0aba85d1bf224988ae2f34ae023293b7")
                .modelName("text-embedding-v4")
                .dimensions(DIMENSION)
                .build();
    }

    // ====================== 测试：存入文本 + 相似度检索 ======================
    public static void main(String[] args) {

//        // 初始化
//        EmbeddingStore<TextSegment> embeddingStore = createMilvusStore();
//        EmbeddingModel embeddingModel = createEmbeddingModel();
//
//        // 1. 准备文档
//        Document document = Document.from("\n" +
//                "### \uD83D\uDCC5 第一天：向量库功能完善与联调\n" +
//                "* **[ ] 任务 1：完成 PgVector 查询逻辑**\n" +
//                "    * 将 `query` 接口中的 `embeddingStore.search` 逻辑补全。\n" +
//                "    * 测试 `minScore` 阈值，确保能够准确检索出存入的文本块。\n" +
//                "* **[ ] 任务 2：实现简单的 RAG 问答接口**\n" +
//                "    * 编写 `chatWithRag` 接口，将检索到的上下文（Context）拼接到 Prompt 中。\n" +
//                "    * 验证通义千问（Qwen）模型是否能基于检索出的内容回答问题。\n" +
//                "* **[ ] 任务 3：本地数据库检查**\n" +
//                "    * 使用 SQL 检查 `embedding` 表的存储结构，确认 `vector` 维度是否与 `EmbeddingModel` 输出一致。\n" +
//                "\n" +
//                "### \uD83D\uDCC5 第二天：系统优化与异常处理\n" +
//                "* **[ ] 任务 1：增加 Redis 缓存容错**\n" +
//                "    * 优化 `summary` 接口的 Redis 逻辑，处理缓存击穿后的降级策略。\n" +
//                "    * 调整流式输出（SSE）的延迟时间（Duration），确保前端交互体验流畅。\n" +
//                "* **[ ] 任务 2：优化文本分块策略**\n" +
//                "    * 针对医疗类或技术类长文，尝试调整 `DocumentSplitter` 的 `maxSegmentSize`（如 800）和 `overlap`（如 100），防止语义被切断。\n" +
//                "* **[ ] 任务 3：技术复习 - JVM 相关**\n" +
//                "    * 复习 G1 垃圾回收器的原理及调优参数。\n" +
//                "    * 回顾 `ThreadLocal` 的内存泄漏场景，结合项目中 `AgentService` 的使用场景进行思考。\n" +
//                "\n" +
//                "### \uD83D\uDCC5 第三天：面试模拟与文档整理\n" +
//                "* **[ ] 任务 1：整理 AI 项目经验**\n" +
//                "    * 总结“基于 RAG 的医生助手”和“博客文章总结”项目的技术难点（如：向量检索的准确率、大模型生成稳定性）。\n" +
//                "    * 梳理如何在没有第三方中间件的情况下实现数据库水平分库的逻辑。\n" +
//                "* **[ ] 任务 2：分布式系统技术储备**\n" +
//                "    * 回顾 RocketMQ 事务消息的实现流程。\n" +
//                "    * 复习 ZooKeeper 注册中心在高并发下的选主机制。\n" +
//                "* **[ ] 任务 3：更新个人简历/作品集**\n" +
//                "    * 将最新的 LangChain4j + Spring AI 整合经验加入简历。\n" +
//                "    * 在本地运行一次完整的 `summary` -> `buildVector` -> `query` 流程，确保 Demo 演示稳定。\n");
//
//        // 2. 文档切分
//        DocumentSplitter splitter = DocumentSplitters.recursive(500, 100);
//        var segments = splitter.split(document);
//
//        // 3. 向量化并存入 Milvus
//        for (TextSegment segment : segments) {
//            //指定tag
//            segment.metadata().put("tag", "每日待办");
//            Embedding embedding = embeddingModel.embed(segment).content();
//            embeddingStore.add(embedding, segment);
//        }
//        System.out.println("✅ 文本已存入 Milvus");
//
//        // 4. 相似度检索（测试）
//        String query = "我后天要做什么";
//        Embedding queryEmbedding = embeddingModel.embed(query).content();
//        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
//                .queryEmbedding(queryEmbedding)
//                .maxResults(1)
//                .build();
//        EmbeddingSearchResult<TextSegment> results = embeddingStore.search(embeddingSearchRequest);
//        EmbeddingMatch<TextSegment> textSegmentEmbeddingMatch = results.matches().get(0);
//        System.out.println("🔍 检索结果：");
////        results.forEach(result -> System.out.println(result.embedded().text()));
    }
}