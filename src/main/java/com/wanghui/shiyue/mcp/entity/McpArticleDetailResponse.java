package com.wanghui.shiyue.mcp.entity;

import lombok.Data;

@Data
public class McpArticleDetailResponse {

    private Long articleId;

    private Long catalogId;

    private String title;

    private String summary;

    private String content;
}
