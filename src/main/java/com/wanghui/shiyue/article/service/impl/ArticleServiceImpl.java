package com.wanghui.shiyue.article.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wanghui.shiyue.agent.mq.ArticleSummaryListener;
import com.wanghui.shiyue.article.convert.ArticleConvert;
import com.wanghui.shiyue.article.convert.TagConvert;
import com.wanghui.shiyue.article.dao.ArticleMapper;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.ArticleQueryParam;
import com.wanghui.shiyue.article.entity.dto.CatalogDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.enums.ArticleStatus;
import com.wanghui.shiyue.article.entity.po.ArticlePO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.ArticleTagService;
import com.wanghui.shiyue.article.service.ArticleCatalogService;
import com.wanghui.shiyue.article.service.TagService;
import com.wanghui.shiyue.comm.kafka.component.MessageProducer;
import com.wanghui.shiyue.comm.utils.IdGenerator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

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

    @Resource
    ArticleCatalogService articleCatalogService;

    @Resource
    MessageProducer messageProducer;

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
        // 多条件查询文章
        List<ArticlePO> articlePOS = articleMapper.queryByMultiCondition(
                param.getClassId(),
                param.getTagIds(),
                param.getCatalogId()
        );
        
        // 转换为DTO并设置标签信息
        List<ArticleDTO> articleDTOS = articleConvert.posToDto(articlePOS);
        return setTagList(articleDTOS);
    }

    @Override
    @Transactional
    public ArticleDTO save(ArticleDTO articleDTO) {
        // 转换为PO对象
        ArticlePO articlePO = articleConvert.dtoToPo(articleDTO);
        articlePO.init();

        // 保存文章
        boolean saveResult;
        if (articlePO.getArticleId() != null) {
            // 更新操作
            saveResult = articleMapper.updateById(articlePO) > 0;
            // 删除旧的标签关联
            articleTagService.deleteByArticleId(articlePO.getArticleId());
            // 删除旧的目录关联
            articleCatalogService.deleteByArticleId(articlePO.getArticleId());
        } else {
            articlePO.setArticleId(IdGenerator.generator());
            // 新增操作
            saveResult = articleMapper.insert(articlePO) > 0;
        }

        // 保存标签关联
        if (saveResult && articleDTO.getTagList() != null && !articleDTO.getTagList().isEmpty()) {
            List<Long> tagIds = articleDTO.getTagList().stream()
                    .filter(tag -> tag.getTagId() != null)
                    .map(TagDTO::getTagId)
                    .collect(Collectors.toList());
            if (!tagIds.isEmpty()) {
                articleTagService.saveArticleTags(articlePO.getArticleId(), tagIds);
            }
        }

        // 保存目录关联
        if (saveResult && articleDTO.getCatalogId() != null) {
            articleCatalogService.saveArticleCatalog(articlePO.getArticleId(), articleDTO.getCatalogId());
        }

        //发送mq，让模型生成摘要,仅仅对保存的才生效
        if(ArticleStatus.DRAFT.getCode().equals(articlePO.getStatus())){
            messageProducer.send(ArticleSummaryListener.TOPIC, articleConvert.poToDto(articlePO));
        }

        return articleConvert.poToDto(articlePO);
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