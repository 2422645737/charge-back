package com.wanghui.shiyue.article.controller;

import com.wanghui.shiyue.article.entity.dto.ClassDTO;
import com.wanghui.shiyue.article.service.ClassService;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping(value = "class")
public class ClassController {
    @Resource
    ClassService classService;

    @Operation(summary = "通过tag获取文章内容")
    @GetMapping("findAllClass")
    public ResponseResult<List<ClassDTO>> findAllClass(){
        return ResponseResult.success(classService.findAllClass());
    }
}