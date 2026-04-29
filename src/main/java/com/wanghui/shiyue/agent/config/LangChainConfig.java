package com.wanghui.shiyue.agent.config;

import com.wanghui.shiyue.agent.config.memory.MemoryConfig;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import com.wanghui.shiyue.agent.service.Assistant;
import com.wanghui.shiyue.agent.handler.FunctionCalling;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

    @Resource
    ChatMemoryProvider chatMemoryProvider;

    /**
     *
     *定义助手，支持工具调用
     * @param qwenChatModel
     * @param functionCalling
     * @return {@link Assistant }
     */

    @Bean
    public Assistant assistant(ChatModel qwenChatModel, FunctionCalling functionCalling) {
        return AiServices.builder(Assistant.class)
                .chatModel(qwenChatModel)
                .tools(functionCalling)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    /**
     * 向量化模型
     * @return {@link OpenAiEmbeddingModel }
     */
    @Bean
    public OpenAiEmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey("sk-0aba85d1bf224988ae2f34ae023293b7")
                .modelName("text-embedding-v4")
                .dimensions(1536)
                .build();
    }

    // 2. pgVector 向量存储
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .host("124.222.67.25")
                .port(5432)
                .database("postgres")
                .user("postgres")
                .password("wanghui")
                .table("article_docs")
                .dimension(1536)
                .build();
    }
}