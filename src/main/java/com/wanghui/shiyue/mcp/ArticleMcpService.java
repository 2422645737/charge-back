package com.wanghui.shiyue.mcp;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.ArticleQueryParam;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.TagService;
import com.wanghui.shiyue.mcp.entity.McpArticleContentQueryRequest;
import com.wanghui.shiyue.mcp.entity.McpArticleContentQueryResponse;
import com.wanghui.shiyue.mcp.entity.McpArticleDetailRequest;
import com.wanghui.shiyue.mcp.entity.McpArticleDetailResponse;
import com.wanghui.shiyue.mcp.entity.McpArticleListRequest;
import com.wanghui.shiyue.mcp.entity.McpArticleListResponse;
import com.wanghui.shiyue.mcp.entity.McpArticleSummaryItem;
import com.wanghui.shiyue.mcp.entity.McpHealthCheckRequest;
import com.wanghui.shiyue.mcp.entity.McpHealthCheckResponse;
import com.wanghui.shiyue.mcp.entity.McpTagItem;
import com.wanghui.shiyue.mcp.entity.McpTagListQueryRequest;
import com.wanghui.shiyue.mcp.entity.McpTagListQueryResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class ArticleMcpService {

    @Resource
    private ArticleService articleService;

    @Resource
    private TagService tagService;

    @Tool(description = "企业级MCP健康检查，返回服务名、版本、状态与时间戳")
    public McpHealthCheckResponse healthCheck(McpHealthCheckRequest request) {
        McpHealthCheckResponse response = new McpHealthCheckResponse();
        response.setServerName("shiyue-mcp-server");
        response.setVersion("1.0.0");
        response.setStatus("UP");
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    @Tool(description = "查询文章摘要列表，支持按目录过滤与数量限制")
    public McpArticleListResponse getTodayArticleList(McpArticleListRequest request) {
        ArticleQueryParam queryParam = new ArticleQueryParam();
        if (request != null) {
            queryParam.setCatalogId(request.getCatalogId());
        }

        List<ArticleDTO> allArticles = articleService.find(queryParam);
        if (CollectionUtils.isEmpty(allArticles)) {
            McpArticleListResponse empty = new McpArticleListResponse();
            empty.setTimestamp(System.currentTimeMillis());
            empty.setTotal(0);
            empty.setArticles(Collections.emptyList());
            return empty;
        }

        int limit = request == null || request.getLimit() == null ? 10 : request.getLimit();
        limit = Math.min(Math.max(limit, 1), 50);

        List<McpArticleSummaryItem> items = allArticles.stream()
                .limit(limit)
                .map(this::toSummaryItem)
                .collect(Collectors.toList());

        McpArticleListResponse response = new McpArticleListResponse();
        response.setTimestamp(System.currentTimeMillis());
        response.setTotal(items.size());
        response.setArticles(items);
        return response;
    }

    @Tool(description = "按文章ID查询文章详情")
    public McpArticleDetailResponse getArticleDetail(McpArticleDetailRequest request) {
        if (request == null || request.getArticleId() == null) {
            return null;
        }
        ArticleDTO articleDTO = articleService.findById(request.getArticleId());
        if (articleDTO == null) {
            return null;
        }
        McpArticleDetailResponse response = new McpArticleDetailResponse();
        response.setArticleId(articleDTO.getArticleId());
        response.setCatalogId(articleDTO.getCatalogId());
        response.setTitle(articleDTO.getTitle());
        response.setSummary(articleDTO.getSummary());
        response.setContent(articleDTO.getContent());
        return response;
    }

    @Tool(description = "根据文章ID查询文章正文内容")
    public McpArticleContentQueryResponse queryArticleContentById(McpArticleContentQueryRequest request) {
        if (request == null || request.getArticleId() == null) {
            return null;
        }
        ArticleDTO articleDTO = articleService.findById(request.getArticleId());
        if (articleDTO == null) {
            return null;
        }
        McpArticleContentQueryResponse response = new McpArticleContentQueryResponse();
        response.setArticleId(articleDTO.getArticleId());
        response.setTitle(articleDTO.getTitle());
        response.setContent(articleDTO.getContent());
        return response;
    }

    @Tool(description = "查询系统全部标签列表")
    public McpTagListQueryResponse queryAllTags(McpTagListQueryRequest request) {
        List<TagDTO> tagList = tagService.findAllTags();
        if (CollectionUtils.isEmpty(tagList)) {
            McpTagListQueryResponse response = new McpTagListQueryResponse();
            response.setTotal(0);
            response.setTags(Collections.emptyList());
            return response;
        }
        List<McpTagItem> items = tagList.stream().map(tag -> {
            McpTagItem item = new McpTagItem();
            item.setTagId(tag.getTagId());
            item.setTagName(tag.getTagName());
            return item;
        }).collect(Collectors.toList());

        McpTagListQueryResponse response = new McpTagListQueryResponse();
        response.setTotal(items.size());
        response.setTags(items);
        return response;
    }

    private McpArticleSummaryItem toSummaryItem(ArticleDTO articleDTO) {
        McpArticleSummaryItem item = new McpArticleSummaryItem();
        item.setArticleId(articleDTO.getArticleId());
        item.setCatalogId(articleDTO.getCatalogId());
        item.setTitle(articleDTO.getTitle());
        item.setSummary(articleDTO.getSummary());
        return item;
    }
}
