package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.ArticleQueryParam;
import com.wanghui.shiyue.article.entity.dto.TagDTO;

import java.util.List;

/**
 * @description: 文章接口
 * @fileName: ArticleService
 * @author: wanghui
 * @createAt: 2023/12/14 03:49:54
 * @updateBy:
 */
public interface ArticleService {

    /**
     * 通过文章id获取文章内容
     * @param articleId
     * @return {@link ArticleDTO }
     */
    ArticleDTO findById(Long articleId);

    /**
     * 通过tag查询文章
     * @param tags
     * @return {@link List }<{@link ArticleDTO }>
     */

    List<ArticleDTO> findByTags(List<String> tags);

    /**
     * 通过文章类型查询文章
     * @param classId
     * @return {@link List }<{@link ArticleDTO }>
     */

    List<ArticleDTO> findByClassId(Long classId);

    /**
     * 多条件查询
     * @param param
     * @return {@link List }<{@link ArticleDTO }>
     */

    List<ArticleDTO> find(ArticleQueryParam param);

    /**
     * 保存文章
     * @param articleDTO
     * @return {@link Boolean }
     */
    Boolean save(ArticleDTO articleDTO);
}