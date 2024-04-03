package com.wanghui.shiyue.article.controller;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.TagService;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@Api(tags = "文章标签控制器")
@RequestMapping(value = "tag")
public class TagController {
    @Resource
    TagService tagService;

    @ApiOperation(value = "获取所有标签")
    @GetMapping("findAllTags")
    public ResponseResult<List<TagDTO>> findAllTags(){
        return ResponseResult.success(tagService.findAllTags());
    }

    @ApiOperation(value = "新增标签")
    @PostMapping("addNewTag")
    public ResponseResult addNewTag(@RequestBody TagDTO tagDTO){
        return tagService.addNewTag(tagDTO);
    }

    @ApiOperation(value = "根据文章id获取其标签信息")
    @GetMapping("getArticleTags")
    public ResponseResult<List<TagDTO>> getArticleTags(@RequestParam("articleId")Long articleId){
        return ResponseResult.success(tagService.getArticleTags(articleId));
    }

    @ApiOperation(value = "获取某个标签下文章数量")
    @PostMapping("tagsCount")
    public ResponseResult<List<TagDTO>> tagsCount(@RequestBody List<Long> tagIds){
        return ResponseResult.success(tagService.tagsCount(tagIds));
    }
}
