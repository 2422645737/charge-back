package com.wanghui.shiyue.article.controller;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.CommentDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.CommentService;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @description: 评论控制器
 * @fileName: CommentController
 * @author: wanghui
 * @createAt: 2024/02/19 11:14:11
 * @updateBy:
 * @copyright: 众阳健康
 */
@RestController
@Tag(name = "评论控制器")
@RequestMapping(value = "comment")
public class CommentController {
    @Resource
    CommentService commentService;

    @Operation(summary = "通过id获取文章评论")
    @GetMapping("findCommentByArticleId")
    public ResponseResult<List<CommentDTO>> findCommentByArticleId(@RequestParam("articleId") Long articleId){
        return ResponseResult.success(commentService.findCommentByArticleId(articleId));
    }
}