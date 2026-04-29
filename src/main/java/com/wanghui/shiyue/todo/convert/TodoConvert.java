package com.wanghui.shiyue.todo.convert;

import com.wanghui.shiyue.todo.entity.dto.TodoDTO;
import com.wanghui.shiyue.todo.entity.po.TodoPO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoConvert {
    TodoDTO poToDto(TodoPO po);
    TodoPO dtoToPo(TodoDTO dto);
    List<TodoDTO> posToDto(List<TodoPO> pos);
}
