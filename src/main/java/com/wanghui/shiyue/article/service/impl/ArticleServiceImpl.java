package com.wanghui.shiyue.article.service.impl;

import com.wanghui.shiyue.article.convert.ArticleConvert;
import com.wanghui.shiyue.article.convert.TagConvert;
import com.wanghui.shiyue.article.dao.ArticleMapper;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.ArticlePO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ArticleServiceImpl implements ArticleService {
    @Resource
    ArticleMapper articleMapper;

    @Resource
    ArticleConvert articleConvert;

    @Resource
    TagService tagService;

    @Resource
    TagConvert tagConvert;

    /**
     * 通过文章id获取文章内容
     * @param articleId
     * @return {@link ArticleDTO }
     */
    @Override
    public ArticleDTO findById(Long articleId) {
        ArticleDTO articleDTO = articleConvert.poToDto(articleMapper.selectById(articleId));
        //获取标签信息
        List<TagDTO> articleTags = tagService.getArticleTags(articleId);
        articleDTO.setTagList(articleTags);

        return articleDTO;
    }

}
