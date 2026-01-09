package com.wanghui.shiyue.article.service.impl;

import com.wanghui.shiyue.article.convert.ArticleConvert;
import com.wanghui.shiyue.article.convert.TagConvert;
import com.wanghui.shiyue.article.dao.ArticleMapper;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.CommentDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.CommentService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @fileName: ArticleServiceImpl
 * @author: wanghui
 * @createAt: 2023/12/14 04:00:27
 * @updateBy:
 * @copyright: 众阳健康
 */
@Service
public class CommentServiceImpl implements CommentService {

    /**
     * 通过id获取文章评论
     * @param articleId
     * @return {@link List }<{@link CommentDTO }>
     */
    @Override
    public List<CommentDTO> findCommentByArticleId(Long articleId) {
        return null;
    }
}