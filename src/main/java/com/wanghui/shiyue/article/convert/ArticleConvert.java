package com.wanghui.shiyue.article.convert;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.po.ArticlePO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 文章实体类转化
 * @fileName: ArticleConvert
 * @author: wanghui
 * @createAt: 2024/01/13 07:42:42
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper(componentModel = "spring")
public interface ArticleConvert {

    /**
     * po转化为dto
     * @param articlePO
     * @return {@link ArticleDTO }
     */
    ArticleDTO poToDto(ArticlePO articlePO);

    /**
     * dto转化为po
     * @param articleDTO
     * @return {@link ArticlePO }
     */
    ArticlePO dtoToPo(ArticleDTO articleDTO);

    /**
     * @param articlePOs
     * @return {@link List }<{@link ArticleDTO }>
     */

    List<ArticleDTO> posToDto(List<ArticlePO> articlePOs);
}