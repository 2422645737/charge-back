package com.wanghui.shiyue.agent.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Assistant {

    @SystemMessage("你是一个有用的人工智能助手，可以使用提供的工具回答用户的问题。如果用户询问天气或者时间，请使用提供的工具。")
    String chat(@UserMessage String userMessage);
}
