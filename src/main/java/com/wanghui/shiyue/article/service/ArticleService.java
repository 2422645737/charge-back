package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
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

}
