package com.wanghui.shiyue.mcp.entity;

import lombok.Data;

@Data
public class McpArticleListRequest {

    /**
     * 可选：目录ID
     */
    private Long catalogId;

    /**
     * 返回条数，默认10，最大50
     */
    private Integer limit;
}
