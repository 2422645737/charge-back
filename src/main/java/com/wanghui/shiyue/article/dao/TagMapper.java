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
 * @fileName: TagMapper
 * @author: wanghui
 * @createAt: 2024/02/19 11:35:36
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper
public interface TagMapper extends BaseMapper<TagPO> {
    /**
     * 获取所有的标签信息
     * @return {@link List }<{@link TagDTO }>
     */
    List<TagPO> findAllTags();


    /**
     * 通过名字查找标签
     * @param tagName
     * @return {@link List }<{@link TagPO }>
     */
    List<TagPO> findByName(String tagName);

    /**
     * 根据文章id获取其标签信息
     * @param articleId
     * @return {@link List }<{@link TagPO }>
     */
    List<TagPO> getArticleTags(Long articleId);

    /**
     * 获取某个标签下文章数量
     * @param tagIds
     * @return {@link List }<{@link TagPO }>
     */
    List<TagPO> tagsCount(@Param("tagIds") List<Long> tagIds);
}
