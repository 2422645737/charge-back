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
    private ArticleCatalogConvert articleCatalogConvert;

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
    public Boolean saveArticleCatalog(Long articleId, Long catalogId) {
        if (articleId == null || catalogId == null) {
            return false;
        }

        // 先删除现有关联
        articleCatalogMapper.deleteByArticleId(articleId);

        // 保存新关联
        ArticleCatalogPO articleCatalogPO = new ArticleCatalogPO();
        articleCatalogPO.setArticleId(articleId);
        articleCatalogPO.setCatalogId(catalogId);
        articleCatalogMapper.insert(articleCatalogPO);

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
    public Boolean deleteByCatalogId(Long catalogId) {
        if (catalogId == null) {
            return false;
        }
        return articleCatalogMapper.deleteByCatalogId(catalogId) > 0;
    }
}
