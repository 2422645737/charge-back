package com.wanghui.shiyue.article.service.impl;

import com.wanghui.shiyue.article.convert.ArticleCatalogConvert;
import com.wanghui.shiyue.article.convert.CatalogConvert;
import com.wanghui.shiyue.article.dao.ArticleCatalogMapper;
import com.wanghui.shiyue.article.dao.CatalogMapper;
import com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO;
import com.wanghui.shiyue.article.entity.dto.CatalogDTO;
import com.wanghui.shiyue.article.entity.po.ArticleCatalogPO;
import com.wanghui.shiyue.article.entity.po.CatalogPO;
import com.wanghui.shiyue.article.service.ArticleCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章目录关联服务实现类
 * @description: 实现文章与目录关联关系的管理功能
 * @fileName: ArticleCatalogServiceImpl
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Service
@Slf4j
public class ArticleCatalogServiceImpl implements ArticleCatalogService {

    @Resource
    private ArticleCatalogMapper articleCatalogMapper;

    @Resource
    private CatalogMapper catalogMapper;

    @Resource
    private ArticleCatalogConvert articleCatalogConvert;

    @Resource
    private CatalogConvert catalogConvert;

    @Override
    public List<ArticleCatalogDTO> getByArticleId(Long articleId) {
        List<ArticleCatalogPO> articleCatalogPOS = articleCatalogMapper.selectByArticleId(articleId);
        return articleCatalogConvert.posToDto(articleCatalogPOS);
    }

    @Override
    public List<ArticleCatalogDTO> getByCatalogId(Long catalogId) {
        List<ArticleCatalogPO> articleCatalogPOS = articleCatalogMapper.selectByCatalogId(catalogId);
        return articleCatalogConvert.posToDto(articleCatalogPOS);
    }

    @Override
    @Transactional
    public Boolean saveArticleCatalogs(Long articleId, List<Long> catalogIds) {
        if (articleId == null || catalogIds == null || catalogIds.isEmpty()) {
            return false;
        }

        // 先删除现有关联
        articleCatalogMapper.deleteByArticleId(articleId);

        // 保存新关联
        for (Long catalogId : catalogIds) {
            ArticleCatalogPO articleCatalogPO = new ArticleCatalogPO();
            articleCatalogPO.setArticleId(articleId);
            articleCatalogPO.setCatalogId(catalogId);
            articleCatalogMapper.insert(articleCatalogPO);
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean deleteByArticleId(Long articleId) {
        if (articleId == null) {
            return false;
        }
        return articleCatalogMapper.deleteByArticleId(articleId) > 0;
    }

    @Override
    @Transactional
    public Boolean deleteByArticleIdAndCatalogId(Long articleId, Long catalogId) {
        if (articleId == null || catalogId == null) {
            return false;
        }
        ArticleCatalogPO articleCatalogPO = articleCatalogMapper.selectByArticleIdAndCatalogId(articleId, catalogId);
        if (articleCatalogPO == null) {
            return false;
        }
        return articleCatalogMapper.deleteById(articleCatalogPO.getArticleCatalogId()) > 0;
    }

    @Override
    @Transactional
    public Boolean deleteByCatalogId(Long catalogId) {
        if (catalogId == null) {
            return false;
        }
        return articleCatalogMapper.deleteByCatalogId(catalogId) > 0;
    }

    @Override
    public List<CatalogDTO> getArticleCatalogTree(Long articleId) {
        if (articleId == null) {
            return new ArrayList<>();
        }

        // 获取文章关联的目录
        List<ArticleCatalogPO> articleCatalogPOS = articleCatalogMapper.selectByArticleId(articleId);
        if (articleCatalogPOS.isEmpty()) {
            return new ArrayList<>();
        }

        // 构建目录列表
        List<CatalogPO> catalogPOS = new ArrayList<>();
        for (ArticleCatalogPO articleCatalogPO : articleCatalogPOS) {
            CatalogPO catalogPO = catalogMapper.selectById(articleCatalogPO.getCatalogId());
            if (catalogPO != null) {
                catalogPOS.add(catalogPO);
            }
        }

        // 转换为DTO并构建树状结构
        List<CatalogDTO> catalogDTOS = catalogConvert.posToDto(catalogPOS);
        return buildCatalogTree(catalogDTOS);
    }

    /**
     * 构建目录树状结构
     * @param catalogDTOS 目录DTO列表
     * @return {@link List }<{@link CatalogDTO }>
     */
    private List<CatalogDTO> buildCatalogTree(List<CatalogDTO> catalogDTOS) {
        List<CatalogDTO> rootCatalogs = new ArrayList<>();

        // 构建目录映射
        java.util.Map<Long, CatalogDTO> catalogMap = new java.util.HashMap<>();
        for (CatalogDTO catalogDTO : catalogDTOS) {
            catalogMap.put(catalogDTO.getCatalogId(), catalogDTO);
            catalogDTO.setChildren(new ArrayList<>());
        }

        // 构建目录树
        for (CatalogDTO catalogDTO : catalogDTOS) {
            if (catalogDTO.getParentId() == null || catalogDTO.getParentId() == 0) {
                // 顶级目录
                rootCatalogs.add(catalogDTO);
            } else {
                // 子目录
                CatalogDTO parentCatalog = catalogMap.get(catalogDTO.getParentId());
                if (parentCatalog != null) {
                    parentCatalog.getChildren().add(catalogDTO);
                }
            }
        }

        return rootCatalogs;
    }
}
