package com.wanghui.shiyue.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.ArticlePO;
import com.wanghui.shiyue.article.entity.po.TagPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @fileName: ArticleMapper
 * @author: wanghui
 * @createAt: 2024/01/13 07:22:39
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticlePO> {

}
