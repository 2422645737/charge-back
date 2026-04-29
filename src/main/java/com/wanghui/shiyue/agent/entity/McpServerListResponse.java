package com.wanghui.shiyue.agent.entity;

import lombok.Data;

import java.util.List;

@Data
public class McpServerListResponse {

    private List<McpServerInfo> servers;
}
