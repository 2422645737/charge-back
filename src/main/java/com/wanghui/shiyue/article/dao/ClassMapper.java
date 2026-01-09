package com.wanghui.shiyue.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.article.entity.dto.ClassDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.ClassPO;
import com.wanghui.shiyue.article.entity.po.TagPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @fileName: TagMapper
 * @author: wanghui
 * @createAt: 2024/02/19 11:35:36
 * @updateBy:
 * @copyright: 众阳健康
 */
@Mapper
public interface ClassMapper extends BaseMapper<ClassPO> {

    /**
     * @return {@link List }<{@link ClassPO }>
     */

    List<ClassPO> findAllClass();

    /**
     * 通过classIdList获取文章数量
     * @param classIdList
     * @return {@link List }<{@link ClassDTO }>
     */

    List<ClassDTO> countByClassIdList(@Param("classIdList") List<Long> classIdList);
}