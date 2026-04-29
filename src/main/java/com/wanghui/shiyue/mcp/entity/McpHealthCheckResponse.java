package com.wanghui.shiyue.mcp.entity;

import lombok.Data;

@Data
public class McpHealthCheckResponse {

    private String serverName;

    private String version;

    private String status;

    private Long timestamp;
}
