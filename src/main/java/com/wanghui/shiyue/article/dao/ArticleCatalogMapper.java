package com.wanghui.shiyue.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.article.entity.po.ArticleCatalogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章目录关联数据访问接口
 * @description: 用于文章与目录关联关系的数据库操作，继承BaseMapper并扩展自定义方法
 * @fileName: ArticleCatalogMapper
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper
public interface ArticleCatalogMapper extends BaseMapper<ArticleCatalogPO> {

    /**
     * 根据文章id查询关联的目录
     * @param articleId 文章id
     * @return {@link List }<{@link ArticleCatalogPO }>
     */
    List<ArticleCatalogPO> selectByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据目录id查询关联的文章
     * @param catalogId 目录id
     * @return {@link List }<{@link ArticleCatalogPO }>
     */
    List<ArticleCatalogPO> selectByCatalogId(@Param("catalogId") Long catalogId);

    /**
     * 根据文章id和目录id查询关联关系
     * @param articleId 文章id
     * @param catalogId 目录id
     * @return {@link ArticleCatalogPO }
     */
    ArticleCatalogPO selectByArticleIdAndCatalogId(@Param("articleId") Long articleId, @Param("catalogId") Long catalogId);

    /**
     * 根据文章id删除关联的目录
     * @param articleId 文章id
     * @return int
     */
    int deleteByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据目录id删除关联的文章
     * @param catalogId 目录id
     * @return int
     */
    int deleteByCatalogId(@Param("catalogId") Long catalogId);
}
