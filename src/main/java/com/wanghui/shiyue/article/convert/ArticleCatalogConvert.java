package com.wanghui.shiyue.article.convert;

import com.wanghui.shiyue.article.entity.dto.ArticleCatalogDTO;
import com.wanghui.shiyue.article.entity.po.ArticleCatalogPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 文章目录关联实体类转化
 * @description: 用于在ArticleCatalogPO和ArticleCatalogDTO之间进行转换
 * @fileName: ArticleCatalogConvert
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper(componentModel = "spring")
public interface ArticleCatalogConvert {

    /**
     * po转化为dto
     * @param articleCatalogPO
     * @return {@link ArticleCatalogDTO }
     */
    ArticleCatalogDTO poToDto(ArticleCatalogPO articleCatalogPO);

    /**
     * dto转化为po
     * @param articleCatalogDTO
     * @return {@link ArticleCatalogPO }
     */
    ArticleCatalogPO dtoToPo(ArticleCatalogDTO articleCatalogDTO);

    /**
     * pos转化为dtos
     * @param articleCatalogPOS
     * @return {@link List }<{@link ArticleCatalogDTO }>
     */
    List<ArticleCatalogDTO> posToDto(List<ArticleCatalogPO> articleCatalogPOS);

    /**
     * dtos转化为pos
     * @param articleCatalogDTOS
     * @return {@link List }<{@link ArticleCatalogPO }>
     */
    List<ArticleCatalogPO> dtosToPo(List<ArticleCatalogDTO> articleCatalogDTOS);
}
