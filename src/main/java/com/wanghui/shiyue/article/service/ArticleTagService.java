package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;

import java.util.List;

/**
 * @description:
 * @fileName: ArticleTagService
 * @author: wanghui
 * @createAt: 2025/12/17 04:21:55
 * @updateBy:
 * @copyright:
 */

public interface ArticleTagService {

    /**
     * 通过tagId获取关联文章信息
     * @param tagIds
     * @return {@link List }<{@link TagDTO }>
     */

    List<Long> getArticlesByTags(List<Long> tagIds);

    /**
     * 保存文章标签关联
     * @param articleId
     * @param tagIds
     * @return {@link Boolean }
     */
    Boolean saveArticleTags(Long articleId, List<Long> tagIds);

    /**
     * 根据文章id删除标签关联
     * @param articleId
     * @return {@link Boolean }
     */
    Boolean deleteByArticleId(Long articleId);
}