package com.wanghui.shiyue.agent.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import com.wanghui.shiyue.agent.service.Assistant;
import com.wanghui.shiyue.agent.handler.FunctionCalling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

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
                .build();
    }

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