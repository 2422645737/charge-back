package com.wanghui.shiyue.agent.entity;

import lombok.Data;

import java.util.List;

@Data
public class McpToolListResponse {

    private String serverKey;

    private Integer total;

    private List<McpToolInfo> tools;
}
