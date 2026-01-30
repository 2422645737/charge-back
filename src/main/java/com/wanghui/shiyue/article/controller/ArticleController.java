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
import org.apache.catalina.util.RateLimiter;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Resource
    KafkaTemplate<String,String> kafkaTemplate;

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

    @Operation(summary = "保存文章")
    @PostMapping("save")
    public ResponseResult<ArticleDTO> save(@RequestBody ArticleDTO articleDTO){
        return ResponseResult.success(articleService.save(articleDTO));
    }


    @Operation(summary = "发送消息")
    @GetMapping("send")
    public ResponseResult send(@RequestParam("send") String message){
        for(int i = 0;i < 10000;i++){
            kafkaTemplate.send("test-topic",message);
        }
        return null;
    }


}