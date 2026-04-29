package com.wanghui.shiyue.mcp.entity;

import lombok.Data;

@Data
public class McpArticleSummaryItem {

    private Long articleId;

    private Long catalogId;

    private String title;

    private String summary;
}
