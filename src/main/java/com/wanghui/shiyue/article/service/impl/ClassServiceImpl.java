package com.wanghui.shiyue.article.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.wanghui.shiyue.article.convert.ClassConvert;
import com.wanghui.shiyue.article.convert.TagConvert;
import com.wanghui.shiyue.article.dao.ClassMapper;
import com.wanghui.shiyue.article.dao.TagMapper;
import com.wanghui.shiyue.article.entity.dto.ClassDTO;
import com.wanghui.shiyue.article.entity.dto.TagDTO;
import com.wanghui.shiyue.article.entity.po.ClassPO;
import com.wanghui.shiyue.article.entity.po.TagPO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.article.service.ClassService;
import com.wanghui.shiyue.article.service.TagService;
import com.wanghui.shiyue.comm.codes.BaseCode;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import com.wanghui.shiyue.comm.utils.IdGenerator;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @fileName: TagServiceImpl
 * @author: wanghui
 * @createAt: 2024/02/19 11:15:55
 * @updateBy:
 * @copyright: 众阳健康
 */
@Service
public class ClassServiceImpl implements ClassService {

    @Resource
    ClassMapper classMapper;

    @Resource
    ClassConvert classConvert;

    @Resource
    ArticleService articleService;

    @Override
    public List<ClassDTO> findAllClass() {
        List<ClassPO> allClass = classMapper.findAllClass();

        List<ClassDTO> classList = classConvert.posToDto(allClass);

        //封装文章数量
        List<Long> classIdList = classList.stream().map(ClassDTO::getClassId).collect(Collectors.toList());

        Map<Long, Integer> countMap = count(classIdList);

        classList.forEach(classDTO -> classDTO.setCount(countMap.getOrDefault(classDTO.getClassId(),0)));

        return classList;
    }

    @Override
    public Map<Long,Integer> count(List<Long> classIdList) {
        List<ClassDTO> classDTOS = classMapper.countByClassIdList(classIdList);

        return classDTOS.stream().collect(Collectors.toMap(ClassDTO::getClassId, ClassDTO::getCount));
    }


}