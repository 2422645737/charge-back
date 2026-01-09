package com.wanghui.shiyue.article.controller;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.ArticleQueryParam;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.impl.ArticleServiceImpl;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 文章控制类
 * @fileName: ArticleController
 * @author: wanghui
 * @createAt: 2024/01/12 08:47:45
 * @updateBy:
 * @copyright: 众阳健康
 */
@RestController
@Tag(name = "文章资源控制器")
@RequestMapping(value = "article")
public class ArticleController {
    @Resource
    ArticleService articleService;

    @Operation(summary = "通过id获取文章内容")
    @GetMapping("findById")
    public ResponseResult<ArticleDTO> findById(@RequestParam("articleId") Long articleId){
        return ResponseResult.success(articleService.findById(articleId));
    }

    @Operation(summary = "通过tag获取文章内容")
    @PostMapping("findByTags")
    public ResponseResult<List<ArticleDTO>> findByTags(@RequestBody List<String> tags){
        return ResponseResult.success(articleService.findByTags(tags));
    }

    @Operation(summary = "通过classId获取文章内容")
    @GetMapping("findByClassId")
    public ResponseResult<List<ArticleDTO>> findByClassId(@RequestParam("classId") Long classId){
        return ResponseResult.success(articleService.findByClassId(classId));
    }

    @Operation(summary = "通过tag获取文章内容")
    @PostMapping("find")
    public ResponseResult<List<ArticleDTO>> find(@RequestBody ArticleQueryParam param){
        return ResponseResult.success(articleService.find(param));
    }

}