package com.wanghui.shiyue.agent.config;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfig {

    @Bean(name = "deepseek-stream")
    public StreamingChatModel qwenStreamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey("sk-041b45491c6a4575b688c26e3c93821a")
                .modelName("deepseek-v4-flash")
                .baseUrl("https://api.deepseek.com")
                .build();
    }

}