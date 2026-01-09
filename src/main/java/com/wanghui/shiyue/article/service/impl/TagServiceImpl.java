package com.wanghui.shiyue.article.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.wanghui.shiyue.article.convert.ArticleConvert;
import com.wanghui.shiyue.article.convert.TagConvert;
import com.wanghui.shiyue.article.dao.ArticleMapper;
import com.wanghui.shiyue.article.dao.TagMapper;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.TagPO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.TagService;
import com.wanghui.shiyue.comm.codes.BaseCode;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import com.wanghui.shiyue.comm.utils.IdGenerator;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @fileName: TagServiceImpl
 * @author: wanghui
 * @createAt: 2024/02/19 11:15:55
 * @updateBy:
 * @copyright: 众阳健康
 */
@Service
public class TagServiceImpl implements TagService {
    @Resource
    TagMapper tagMapper;
    @Resource
    TagConvert tagConvert;

    /**
     * 获取所有标签信息
     * @return {@link List }<{@link TagDTO }>
     */
    @Override
    public List<TagDTO> findAllTags() {
        return tagConvert.posToDto(tagMapper.findAllTags());
    }

    /**
     * 新增标签
     * @param tagDTO
     */
    @Override
    public ResponseResult addNewTag(TagDTO tagDTO) {
        //判断是否已经有相同标签
        List<TagPO> byName = tagMapper.findByName(tagDTO.getTagName());
        if(CollUtil.isNotEmpty(byName)){
            return ResponseResult.error(BaseCode.TAG_REPEAT,byName);
        }

        TagPO tagPO = tagConvert.dtoToPo(tagDTO);
        tagPO.setTagId(IdGenerator.generator());
        tagPO.setInvalidFlag(NumberUtils.INTEGER_ZERO.toString());
        tagMapper.insert(tagPO);
        return ResponseResult.success();
    }

    /**
     * 根据文章id获取其标签信息
     * @param articleId
     * @return {@link List }<{@link TagDTO }>
     */
    @Override
    public List<TagDTO> getArticleTags(Long articleId) {
        return tagConvert.posToDto(tagMapper.getArticleTags(articleId));
    }

    /**
     * 获取某个标签下文章数量
     * @param tagIds
     * @return {@link Long }
     */
    @Override
    public List<TagDTO> tagsCount(List<Long> tagIds) {
        List<TagDTO> tagDTOS = tagConvert.posToDto(tagMapper.tagsCount(tagIds));

        return tagDTOS;
    }

    /**
     * 通过标签名批量获取标签信息
     * @param tags
     * @return {@link List }<{@link TagDTO }>
     */

    @Override
    public List<TagDTO> getTagByName(List<String> tags) {
        return tagConvert.posToDto(tagMapper.findByNames(tags));
    }
}