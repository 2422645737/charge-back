package com.wanghui.shiyue.article.controller;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.impl.ArticleServiceImpl;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@Api(tags = "文章资源控制器")
@RequestMapping(value = "article")
public class ArticleController {
    @Resource
    ArticleService articleService;

    @ApiOperation(value = "通过id获取文章内容")
    @GetMapping("findById")
    public ResponseResult<ArticleDTO> findById(@RequestParam("articleId") Long articleId){
        return ResponseResult.success(articleService.findById(articleId));
    }
}
