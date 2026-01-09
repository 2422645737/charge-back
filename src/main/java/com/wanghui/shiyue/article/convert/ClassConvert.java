package com.wanghui.shiyue.article.convert;

import com.wanghui.shiyue.article.entity.dto.ClassDTO;
import com.wanghui.shiyue.article.entity.po.ClassPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @description:
 * @fileName: ClassConvert
 * @author: wanghui
 * @createAt: 2024/02/19 09:21:28
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper(componentModel = "spring")
public interface ClassConvert {

    /**
     * po2dto
     * @param ClassPO
     * @return {@link ClassDTO }
     */
    ClassDTO poToDto(ClassPO ClassPO);

    /**
     * dto2po
     * @param ClassDTO
     * @return {@link ClassPO }
     */
    ClassPO dtoToPo(ClassDTO ClassDTO);

    /**
     * posToDto
     * @param ClassDTO
     * @return {@link ClassPO }
     */
    List<ClassDTO> posToDto(List<ClassPO> ClassDTO);
}