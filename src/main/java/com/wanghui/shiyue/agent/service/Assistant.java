package com.wanghui.shiyue.agent.service;

import com.wanghui.shiyue.agent.entity.SpecialistPrompt;
import com.wanghui.shiyue.agent.entity.User;
import dev.langchain4j.service.*;


public interface Assistant {

    @SystemMessage(fromResource = "prompt/SystemMessage.md")
    String chat(@MemoryId String memoryId, @UserMessage String userMessage);

    /**
     * 支持自定义参数的写法
     * @param memoryId
     * @param userMessage
     * @param name
     * @return {@link String }
     */
    @SystemMessage(fromResource = "prompt/SystemMessage.md")
    @UserMessage("你好，我叫{{name}}，我的问题是：{{userMessage}}")
    String chatWithParam(@MemoryId String memoryId,
                         @V("userMessage") String userMessage,
                         @V("name") String name);


    /**
     * langChain会自动在你的提示词后面拼接一段话，严格按照json返回，返回的json结构就是你的returnType
     * @param memoryId
     * @param userMessage
     * @return {@link User }
     */
    @SystemMessage(fromResource = "prompt/SystemMessage.md")
    User chatWithType(@MemoryId String memoryId, @UserMessage String userMessage);

    @SystemMessage("你是一名专家，你只能回答编程相关问题，如果和编程无关，返回无法回答")
    @UserMessage("你是一名专家，你只能回答编程相关问题，如果和编程无关，返回无法回答")
    String chat(SpecialistPrompt prompt);
}
