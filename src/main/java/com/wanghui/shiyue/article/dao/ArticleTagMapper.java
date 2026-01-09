package com.wanghui.shiyue.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.article.entity.po.ArticlePO;
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
public interface ArticleTagMapper extends BaseMapper<ArticlePO> {

    /**
     * 通过tagId获取关联文章信息
     * @param tagIds
     * @return {@link List }<{@link Long }>
     */

    List<Long> getArticlesByTags(@Param("tagIds") List<Long> tagIds);
}