package com.wanghui.shiyue.article.controller;

import com.wanghui.shiyue.article.entity.dto.CatalogDTO;
import com.wanghui.shiyue.article.service.CatalogService;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 目录控制器
 * @description: 提供目录的CRUD操作和层级关系管理的API接口
 * @fileName: CatalogController
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@RestController
@Tag(name = "目录资源控制器")
@RequestMapping(value = "catalog")
@Slf4j
public class CatalogController {

    @Resource
    private CatalogService catalogService;

    /**
     * 根据目录id获取目录信息
     * @param catalogId 目录id
     * @return ResponseResult<CatalogDTO>
     */
    @Operation(summary = "根据目录id获取目录信息")
    @GetMapping("getById")
    public ResponseResult<CatalogDTO> getById(@RequestParam("catalogId") Long catalogId) {
        return ResponseResult.success(catalogService.getById(catalogId));
    }

    /**
     * 获取所有目录
     * @return ResponseResult<List<CatalogDTO>>
     */
    @Operation(summary = "获取所有目录")
    @GetMapping("getAll")
    public ResponseResult<List<CatalogDTO>> getAll() {
        return ResponseResult.success(catalogService.getAllCatalogs());
    }

    /**
     * 获取目录树状结构
     * @return ResponseResult<List<CatalogDTO>>
     */
    @Operation(summary = "获取目录树状结构")
    @GetMapping("getTree")
    public ResponseResult<List<CatalogDTO>> getTree() {
        return ResponseResult.success(catalogService.getCatalogTree());
    }

    /**
     * 根据父目录id获取子目录
     * @param parentId 父目录id
     * @return ResponseResult<List<CatalogDTO>>
     */
    @Operation(summary = "根据父目录id获取子目录")
    @GetMapping("getChildren")
    public ResponseResult<List<CatalogDTO>> getChildren(@RequestParam("parentId") Long parentId) {
        return ResponseResult.success(catalogService.getChildrenByParentId(parentId));
    }

    /**
     * 保存目录
     * @param catalogDTO 目录DTO
     * @return ResponseResult<Boolean>
     */
    @Operation(summary = "保存目录")
    @PostMapping("save")
    public ResponseResult<Boolean> save(@RequestBody CatalogDTO catalogDTO) {
        return ResponseResult.success(catalogService.save(catalogDTO));
    }

    /**
     * 更新目录
     * @param catalogDTO 目录DTO
     * @return ResponseResult<Boolean>
     */
    @Operation(summary = "更新目录")
    @PutMapping("update")
    public ResponseResult<Boolean> update(@RequestBody CatalogDTO catalogDTO) {
        return ResponseResult.success(catalogService.update(catalogDTO));
    }

    /**
     * 删除目录
     * @param catalogId 目录id
     * @return ResponseResult<Boolean>
     */
    @Operation(summary = "删除目录")
    @DeleteMapping("delete")
    public ResponseResult<Boolean> delete(@RequestParam("catalogId") Long catalogId) {
        return ResponseResult.success(catalogService.delete(catalogId));
    }

    /**
     * 根据目录路径获取目录
     * @param path 目录路径
     * @return ResponseResult<CatalogDTO>
     */
    @Operation(summary = "根据目录路径获取目录")
    @GetMapping("getByPath")
    public ResponseResult<CatalogDTO> getByPath(@RequestParam("path") String path) {
        return ResponseResult.success(catalogService.getByPath(path));
    }

    /**
     * 根据目录层级获取目录
     * @param level 目录层级
     * @return ResponseResult<List<CatalogDTO>>
     */
    @Operation(summary = "根据目录层级获取目录")
    @GetMapping("getByLevel")
    public ResponseResult<List<CatalogDTO>> getByLevel(@RequestParam("level") Integer level) {
        return ResponseResult.success(catalogService.getByLevel(level));
    }
}
