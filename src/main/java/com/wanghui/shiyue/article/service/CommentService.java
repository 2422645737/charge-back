package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.CommentDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.CommentPO;

import java.util.List;

/**
 * @description: 评论服务
 * @fileName: CommentService
 * @author: wanghui
 * @createAt: 2024/02/19 11:15:17
 * @updateBy:
 * @copyright: 众阳健康
 */
public interface CommentService {

    /**
     * 通过id获取文章评论
     * @param articleId
     * @return {@link List }<{@link CommentDTO }>
     */
    List<CommentDTO> findCommentByArticleId(Long articleId);
}
