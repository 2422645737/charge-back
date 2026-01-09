package com.wanghui.shiyue.article.service;

import com.wanghui.shiyue.article.entity.dto.ClassDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.comm.entity.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @fileName: TagService
 * @author: wanghui
 * @createAt: 2024/02/19 11:15:28
 * @updateBy:
 * @copyright: 众阳健康
 */
public interface ClassService {

    /**
     * 查询所有的文章类别
     * @return {@link List }<{@link ClassDTO }>
     */

    List<ClassDTO> findAllClass();

    /**
     * 统计指定classId的文章数量
     * key为classId，value为文章数量
     * @param classIdList
     * @return {@link List }<{@link ClassDTO }>
     */

    Map<Long,Integer> count(List<Long> classIdList);
}