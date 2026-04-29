package com.wanghui.shiyue.todo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.todo.entity.po.TodoLogPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoLogMapper extends BaseMapper<TodoLogPO> {
}
