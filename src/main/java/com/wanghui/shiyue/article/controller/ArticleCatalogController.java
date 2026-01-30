package com.wanghui.shiyue.article.controller;

import com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO;
import com.wanghui.shiyue.article.entity.dto.CatalogDTO;
import com.wanghui.shiyue.article.service.ArticleCatalogService;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 文章目录关联控制器
 * @description: 提供文章与目录关联关系管理的API接口
 * @fileName: ArticleCatalogController
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@RestController
@Tag(name = "文章目录关联控制器")
@RequestMapping(value = "article-catalog")
@Slf4j
public class ArticleCatalogController {

    @Resource
    private ArticleCatalogService articleCatalogService;

    /**
     * 根据文章id获取关联的目录
     * @param articleId 文章id
     * @return ResponseResult<List<ArticleCatalogDTO>>
     */
    @Operation(summary = "根据文章id获取关联的目录")
    @GetMapping("getByArticleId")
    public ResponseResult<List<ArticleCatalogDTO>> getByArticleId(@RequestParam("articleId") Long articleId) {
        return ResponseResult.success(articleCatalogService.getByArticleId(articleId));
    }

    /**
     * 根据目录id获取关联的文章
     * @param catalogId 目录id
     * @return ResponseResult<List<ArticleCatalogDTO>>
     */
    @Operation(summary = "根据目录id获取关联的文章")
    @GetMapping("getByCatalogId")
    public ResponseResult<List<ArticleCatalogDTO>> getByCatalogId(@RequestParam("catalogId") Long catalogId) {
        return ResponseResult.success(articleCatalogService.getByCatalogId(catalogId));
    }

    /**
     * 保存文章与目录的关联关系
     * @param articleId 文章id
     * @param catalogId 目录id
     * @return ResponseResult<Boolean>
     */
    @Operation(summary = "保存文章与目录的关联关系")
    @PostMapping("save")
    public ResponseResult<Boolean> save(@RequestParam("articleId") Long articleId, @RequestParam("catalogId") Long catalogId) {
        return ResponseResult.success(articleCatalogService.saveArticleCatalog(articleId, catalogId));
    }

    /**
     * 删除文章与目录的关联关系
     * @param articleId 文章id
     * @return ResponseResult<Boolean>
     */
    @Operation(summary = "删除文章与目录的关联关系")
    @DeleteMapping("deleteByArticleId")
    public ResponseResult<Boolean> deleteByArticleId(@RequestParam("articleId") Long articleId) {
        return ResponseResult.success(articleCatalogService.deleteByArticleId(articleId));
    }

    /**
     * 根据目录id删除关联的文章
     * @param catalogId 目录id
     * @return ResponseResult<Boolean>
     */
    @Operation(summary = "根据目录id删除关联的文章")
    @DeleteMapping("deleteByCatalogId")
    public ResponseResult<Boolean> deleteByCatalogId(@RequestParam("catalogId") Long catalogId) {
        return ResponseResult.success(articleCatalogService.deleteByCatalogId(catalogId));
    }
}
