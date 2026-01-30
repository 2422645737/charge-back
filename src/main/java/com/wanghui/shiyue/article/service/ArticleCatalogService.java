package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO;
import com.wanghui.shiyue.article.entity.dto.CatalogDTO;

import java.util.List;

/**
 * 文章目录关联服务接口
 * @description: 提供文章与目录关联关系的管理功能
 * @fileName: ArticleCatalogService
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
public interface ArticleCatalogService {

    /**
     * 根据文章id获取关联的目录
     * @param articleId 文章id
     * @return {@link List }<{@link ArticleCatalogDTO }>
     */
    List<ArticleCatalogDTO> getByArticleId(Long articleId);

    /**
     * 根据目录id获取关联的文章
     * @param catalogId 目录id
     * @return {@link List }<{@link ArticleCatalogDTO }>
     */
    List<ArticleCatalogDTO> getByCatalogId(Long catalogId);

    /**
     * 保存文章与目录的关联关系
     * @param articleId 文章id
     * @param catalogId 目录id
     * @return {@link Boolean }
     */
    Boolean saveArticleCatalog(Long articleId, Long catalogId);

    /**
     * 删除文章与目录的关联关系
     * @param articleId 文章id
     * @return {@link Boolean }
     */
    Boolean deleteByArticleId(Long articleId);

    /**
     * 根据目录id删除关联的文章
     * @param catalogId 目录id
     * @return {@link Boolean }
     */
    Boolean deleteByCatalogId(Long catalogId);
}
