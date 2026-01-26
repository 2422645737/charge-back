package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.CatalogDTO;

import java.util.List;

/**
 * 目录服务接口
 * @description: 提供目录的CRUD操作和层级关系处理功能
 * @fileName: CatalogService
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
public interface CatalogService {

    /**
     * 根据目录id获取目录信息
     * @param catalogId 目录id
     * @return {@link CatalogDTO }
     */
    CatalogDTO getById(Long catalogId);

    /**
     * 获取所有目录
     * @return {@link List }<{@link CatalogDTO }>
     */
    List<CatalogDTO> getAllCatalogs();

    /**
     * 获取目录树状结构
     * @return {@link List }<{@link CatalogDTO }>
     */
    List<CatalogDTO> getCatalogTree();

    /**
     * 根据父目录id获取子目录
     * @param parentId 父目录id
     * @return {@link List }<{@link CatalogDTO }>
     */
    List<CatalogDTO> getChildrenByParentId(Long parentId);

    /**
     * 保存目录
     * @param catalogDTO 目录DTO
     * @return {@link Boolean }
     */
    Boolean save(CatalogDTO catalogDTO);

    /**
     * 更新目录
     * @param catalogDTO 目录DTO
     * @return {@link Boolean }
     */
    Boolean update(CatalogDTO catalogDTO);

    /**
     * 删除目录
     * @param catalogId 目录id
     * @return {@link Boolean }
     */
    Boolean delete(Long catalogId);

    /**
     * 根据目录路径获取目录
     * @param path 目录路径
     * @return {@link CatalogDTO }
     */
    CatalogDTO getByPath(String path);

    /**
     * 根据目录层级获取目录
     * @param level 目录层级
     * @return {@link List }<{@link CatalogDTO }>
     */
    List<CatalogDTO> getByLevel(Integer level);
}
