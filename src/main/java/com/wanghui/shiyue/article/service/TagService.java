package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.comm.entity.ResponseResult;

import java.util.List;

/**
 * @description:
 * @fileName: TagService
 * @author: wanghui
 * @createAt: 2024/02/19 11:15:28
 * @updateBy:
 * @copyright: 众阳健康
 */
public interface TagService {

    /**
     * 获取所有标签
     * @return {@link List }<{@link TagDTO }>
     */
    List<TagDTO> findAllTags();


    /**
     * 添加标签
     * @param tagDTO
     * @return {@link ResponseResult }
     */
    ResponseResult addNewTag(TagDTO tagDTO);

    /**
     * 根据文章id获取其标签信息
     * @param articleId
     * @return {@link List }<{@link TagDTO }>
     */
    List<TagDTO> getArticleTags(Long articleId);

    /**
     * 获取某个标签下文章数量
     * @param tagIds
     * @return {@link List }<{@link TagDTO }>
     */
    List<TagDTO> tagsCount(List<Long> tagIds);

    /**
     * 通过tag名称获取tag
     * @param tags
     * @return {@link List }<{@link TagDTO }>
     */

    List<TagDTO> getTagByName(List<String> tags);
}