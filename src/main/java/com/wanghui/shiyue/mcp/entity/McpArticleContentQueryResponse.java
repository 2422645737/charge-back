package com.wanghui.shiyue.mcp.entity;

import lombok.Data;

@Data
public class McpArticleContentQueryResponse {

    private Long articleId;

    private String title;

    private String content;
}
