package com.wanghui.shiyue.todo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.shiyue.todo.entity.dto.TodoDTO;
import com.wanghui.shiyue.todo.entity.po.TodoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TodoMapper extends BaseMapper<TodoPO> {
    List<TodoPO> queryByFilters(@Param("completed") Boolean completed,
                                @Param("priority") Integer priority,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime,
                                @Param("orderByPriority") Boolean orderByPriority);
}
