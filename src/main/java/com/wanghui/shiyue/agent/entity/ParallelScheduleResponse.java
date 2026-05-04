package com.wanghui.shiyue.agent.entity;

import java.util.Map;
import lombok.Data;

@Data
public class ParallelScheduleResponse {

    private String sessionId;

    /** 各专家角色的并行输出 key=专家角色名, value=专家意见 */
    private Map<String, String> expertOpinions;

    /** 综合汇总后的最终回答 */
    private String finalAnswer;

    /** 总耗时(ms) */
    private Long costMs;
}
