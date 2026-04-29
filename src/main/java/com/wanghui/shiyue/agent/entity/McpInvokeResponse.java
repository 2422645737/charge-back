package com.wanghui.shiyue.agent.entity;

import lombok.Data;

@Data
public class McpInvokeResponse {

    private String serverKey;

    private String answer;

    private Long costMs;
}
