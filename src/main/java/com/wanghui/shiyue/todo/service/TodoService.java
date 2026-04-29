package com.wanghui.shiyue.todo.service;

import com.wanghui.shiyue.comm.entity.ResponseResult;
import com.wanghui.shiyue.todo.entity.dto.SubTaskDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoQueryParam;

import java.util.List;

public interface TodoService {
    List<TodoDTO> query(TodoQueryParam param);
    TodoDTO save(TodoDTO dto);
    TodoDTO update(TodoDTO dto);
    Boolean delete(Long todoId);
    Boolean deleteBatch(List<Long> ids);

    List<SubTaskDTO> listSubTasks(Long todoId);
    SubTaskDTO addSubTask(SubTaskDTO dto);
    SubTaskDTO updateSubTask(SubTaskDTO dto);
    Boolean deleteSubTask(Long subTaskId);
}
