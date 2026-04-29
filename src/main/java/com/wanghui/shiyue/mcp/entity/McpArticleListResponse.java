package com.wanghui.shiyue.mcp.entity;

import lombok.Data;

import java.util.List;

@Data
public class McpArticleListResponse {

    private Integer total;

    private Long timestamp;

    private List<McpArticleSummaryItem> articles;
}
