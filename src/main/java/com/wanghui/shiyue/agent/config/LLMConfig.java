package com.wanghui.shiyue.agent.config;

import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

@Configuration
public class LLMConfig {
    // @Bean(name = "qwen")
    // public ChatModel qwen(){
    //     return OpenAiChatModel.builder()
    //             .apiKey("sk-0aba85d1bf224988ae2f34ae023293b7")
    //             .modelName("qwen-max")
    //             .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
    //             .build();
    // }

    // @Bean(name = "deepseek")
    // public ChatModel deepseek(){
    //     return OpenAiChatModel.builder()
    //             .apiKey("sk-041b45491c6a4575b688c26e3c93821a")
    //             .modelName("deepseek-v4-flash")
    //             .baseUrl("https://api.deepseek.com")
    //             .build();
    // }




}
