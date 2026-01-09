package com.wanghui.shiyue.article.service.impl;

import com.wanghui.shiyue.article.convert.ArticleConvert;
import com.wanghui.shiyue.article.convert.TagConvert;
import com.wanghui.shiyue.article.dao.ArticleMapper;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.ArticleQueryParam;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.ArticlePO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.ArticleTagService;
import com.wanghui.shiyue.article.service.TagService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    ArticleTagService articleTagService;

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

    @Override
    public List<ArticleDTO> findByTags(List<String> tags) {
        List<TagDTO> tagByName = tagService.getTagByName(tags);

        List<Long> tagIds = tagByName.stream().map(TagDTO::getTagId).collect(Collectors.toList());

        //通过tag获取文章id
        List<Long> articleIds = articleTagService.getArticlesByTags(tagIds);

        List<ArticlePO> articlePOS = articleMapper.selectBatchIds(articleIds);

        List<ArticleDTO> result = articleConvert.posToDto(articlePOS);

        //为文章封装标签信息
        setTagList(result);

        return result;
    }

    @Override
    public List<ArticleDTO> findByClassId(Long classId) {
        List<ArticlePO> query = articleMapper.queryByClassId(classId);
        return setTagList(articleConvert.posToDto(query));
    }

    @Override
    public List<ArticleDTO> find(ArticleQueryParam param) {

        return null;
    }

    /**
     * 给文章设置标签
     * @param articleDTOS
     * @return {@link List }<{@link ArticleDTO }>
     */

    private List<ArticleDTO> setTagList(List<ArticleDTO> articleDTOS) {
        articleDTOS.forEach(a -> {
            a.setTagList(tagService.getArticleTags(a.getArticleId()));
        });
        return articleDTOS;
    }

}