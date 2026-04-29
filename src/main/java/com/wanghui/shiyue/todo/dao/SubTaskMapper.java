package com.wanghui.shiyue.todo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.todo.entity.po.SubTaskPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubTaskMapper extends BaseMapper<SubTaskPO> {
    List<SubTaskPO> listByTodoId(@Param("todoId") Long todoId);
    List<SubTaskPO> listByParentId(@Param("parentId") Long parentId);
}
