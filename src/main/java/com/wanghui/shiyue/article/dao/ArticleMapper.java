package com.wanghui.shiyue.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.ArticlePO;
import com.wanghui.shiyue.article.entity.po.TagPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @fileName: ArticleMapper
 * @author: wanghui
 * @createAt: 2024/01/13 07:22:39
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticlePO> {

    /**
     * 通过classId查询文章
     * @param classId
     * @return {@link List }<{@link ArticlePO }>
     */

    List<ArticlePO> queryByClassId(@Param("classId") Long classId);
    
    /**
     * 通过目录id查询文章
     * @param catalogId
     * @return {@link List }<{@link ArticlePO }>
     */
    List<ArticlePO> queryByCatalogId(@Param("catalogId") Long catalogId);
    
    /**
     * 通过目录ids查询文章
     * @param catalogIds
     * @return {@link List }<{@link ArticlePO }>
     */
    List<ArticlePO> queryByCatalogIds(@Param("catalogIds") List<Long> catalogIds);
    
    /**
     * 多条件查询文章
     * @param classId
     * @param tagIds
     * @param catalogId
     * @param catalogIds
     * @param catalogLevel
     * @param catalogPath
     * @return {@link List }<{@link ArticlePO }>
     */
    List<ArticlePO> queryByMultiCondition(
            @Param("classId") Long classId,
            @Param("tagIds") List<Long> tagIds,
            @Param("catalogId") Long catalogId,
            @Param("catalogIds") List<Long> catalogIds,
            @Param("catalogLevel") Integer catalogLevel,
            @Param("catalogPath") String catalogPath
    );
}