package com.wanghui.shiyue.agent.entity;

import lombok.Data;

@Data
public class McpInvokeRequest {

    /**
     * 可选，不传时走默认配置
     */
    private String serverKey;

    /**
     * 用户问题
     */
    private String question;
}
