package com.wanghui.shiyue.agent.entity;

import lombok.Data;

@Data
public class PipelineScheduleRequest {

    /**
     * 会话ID，用于隔离记忆上下文；为空时使用默认会话。
     */
    private String sessionId;

    /**
     * 用户输入问题。
     */
    private String userInput;
}
