package com.wanghui.shiyue.todo.convert;

import com.wanghui.shiyue.todo.entity.dto.SubTaskDTO;
import com.wanghui.shiyue.todo.entity.po.SubTaskPO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubTaskConvert {
    SubTaskDTO poToDto(SubTaskPO po);
    SubTaskPO dtoToPo(SubTaskDTO dto);
    List<SubTaskDTO> posToDto(List<SubTaskPO> pos);
}
