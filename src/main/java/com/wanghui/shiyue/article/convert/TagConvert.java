package com.wanghui.shiyue.article.convert;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.ArticlePO;
import com.wanghui.shiyue.article.entity.po.TagPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @description:
 * @fileName: TagConvert
 * @author: wanghui
 * @createAt: 2024/02/19 09:21:28
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper(componentModel = "spring")
public interface TagConvert {

    /**
     * po2dto
     * @param tagPO
     * @return {@link TagDTO }
     */
    TagDTO poToDto(TagPO tagPO);

    /**
     * dto2po
     * @param tagDTO
     * @return {@link TagPO }
     */
    TagPO dtoToPo(TagDTO tagDTO);

    /**
     * posToDto
     * @param tagDTO
     * @return {@link TagPO }
     */
    List<TagDTO> posToDto(List<TagPO> tagDTO);
}
