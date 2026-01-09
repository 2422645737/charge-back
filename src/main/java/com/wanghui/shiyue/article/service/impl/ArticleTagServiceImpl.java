package com.wanghui.shiyue.article.service.impl;

import com.wanghui.shiyue.article.dao.ArticleTagMapper;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.service.ArticleTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class ArticleTagServiceImpl implements ArticleTagService {

    @Resource
    ArticleTagMapper articleTagMapper;

    @Override
    public List<Long> getArticlesByTags(List<Long> tagIds) {
        return articleTagMapper.getArticlesByTags(tagIds);
    }
}