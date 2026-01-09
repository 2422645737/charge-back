package com.wanghui.shiyue.article.controller;

import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.service.TagService;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 标签
 * @fileName: TagController
 * @author: wanghui
 * @createAt: 2024/02/19 11:14:25
 * @updateBy:
 * @copyright: 众阳健康
 */
@RestController
@Tag(name = "文章标签控制器")
@RequestMapping(value = "tag")
public class TagController {
    @Resource
    TagService tagService;

    @Operation(summary = "获取所有标签")
    @GetMapping("findAllTags")
    public ResponseResult<List<TagDTO>> findAllTags(){
        return ResponseResult.success(tagService.findAllTags());
    }

    @Operation(summary = "新增标签")
    @PostMapping("addNewTag")
    public ResponseResult addNewTag(@RequestBody TagDTO tagDTO){
        return tagService.addNewTag(tagDTO);
    }

    @Operation(summary = "根据文章id获取其标签信息")
    @GetMapping("getArticleTags")
    public ResponseResult<List<TagDTO>> getArticleTags(@RequestParam("articleId")Long articleId){
        return ResponseResult.success(tagService.getArticleTags(articleId));
    }

    @Operation(summary = "获取某个标签下文章数量")
    @PostMapping("tagsCount")
    public ResponseResult<List<TagDTO>> tagsCount(@RequestBody List<Long> tagIds){
        return ResponseResult.success(tagService.tagsCount(tagIds));
    }
}