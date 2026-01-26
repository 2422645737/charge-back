package com.wanghui.shiyue.article.convert;

import com.wanghui.shiyue.article.entity.dto.CatalogDTO;
import com.wanghui.shiyue.article.entity.po.CatalogPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 目录实体类转化
 * @description: 用于在CatalogPO和CatalogDTO之间进行转换
 * @fileName: CatalogConvert
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper(componentModel = "spring")
public interface CatalogConvert {

    /**
     * po转化为dto
     * @param catalogPO
     * @return {@link CatalogDTO }
     */
    CatalogDTO poToDto(CatalogPO catalogPO);

    /**
     * dto转化为po
     * @param catalogDTO
     * @return {@link CatalogPO }
     */
    CatalogPO dtoToPo(CatalogDTO catalogDTO);

    /**
     * pos转化为dtos
     * @param catalogPOS
     * @return {@link List }<{@link CatalogDTO }>
     */
    List<CatalogDTO> posToDto(List<CatalogPO> catalogPOS);

    /**
     * dtos转化为pos
     * @param catalogDTOS
     * @return {@link List }<{@link CatalogPO }>
     */
    List<CatalogPO> dtosToPo(List<CatalogDTO> catalogDTOS);
}
