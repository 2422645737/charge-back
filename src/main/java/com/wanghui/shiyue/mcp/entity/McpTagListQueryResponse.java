package com.wanghui.shiyue.mcp.entity;

import lombok.Data;

import java.util.List;

@Data
public class McpTagListQueryResponse {

    private Integer total;

    private List<McpTagItem> tags;
}
