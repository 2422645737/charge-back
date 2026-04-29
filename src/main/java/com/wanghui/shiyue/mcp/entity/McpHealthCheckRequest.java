package com.wanghui.shiyue.mcp.entity;

import lombok.Data;

@Data
public class McpHealthCheckRequest {

    /**
     * 可选链路追踪ID
     */
    private String traceId;
}
