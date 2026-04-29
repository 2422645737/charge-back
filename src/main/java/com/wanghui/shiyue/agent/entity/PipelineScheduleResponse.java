package com.wanghui.shiyue.agent.entity;

import lombok.Data;

@Data
public class PipelineScheduleResponse {

    private String sessionId;

    /**
     * 第一阶段生成的执行计划。
     */
    private String plan;

    /**
     * 第二阶段给出的最终回答。
     */
    private String answer;

    /**
     * 当前会话中保留的记忆条数。
     */
    private Integer memoryMessageCount;

    private Long costMs;
}
