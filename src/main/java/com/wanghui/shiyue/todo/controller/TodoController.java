package com.wanghui.shiyue.todo.controller;

import com.wanghui.shiyue.comm.entity.ResponseResult;
import com.wanghui.shiyue.todo.entity.dto.SubTaskDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoQueryParam;
import com.wanghui.shiyue.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "待办控制器")
@RequestMapping("todo")
public class TodoController {
    @Resource
    private TodoService todoService;

    @Operation(summary = "查询待办")
    @PostMapping("query")
    public ResponseResult<List<TodoDTO>> query(@RequestBody TodoQueryParam param) {
        return ResponseResult.success(todoService.query(param));
    }

    @Operation(summary = "新增待办")
    @PostMapping("add")
    public ResponseResult<TodoDTO> add(@RequestBody TodoDTO dto) {
        return ResponseResult.success(todoService.save(dto));
    }

    @Operation(summary = "修改待办")
    @PostMapping("update")
    public ResponseResult<TodoDTO> update(@RequestBody TodoDTO dto) {
        return ResponseResult.success(todoService.update(dto));
    }

    @Operation(summary = "删除待办")
    @PostMapping("delete")
    public ResponseResult<Boolean> delete(@RequestParam("todoId") Long todoId) {
        return ResponseResult.success(todoService.delete(todoId));
    }

    @Operation(summary = "批量删除待办")
    @PostMapping("deleteBatch")
    public ResponseResult<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        return ResponseResult.success(todoService.deleteBatch(ids));
    }

    @Operation(summary = "查询子项目")
    @PostMapping("sub/list")
    public ResponseResult<List<SubTaskDTO>> listSub(@RequestParam("todoId") Long todoId) {
        return ResponseResult.success(todoService.listSubTasks(todoId));
    }

    @Operation(summary = "新增子项目")
    @PostMapping("sub/add")
    public ResponseResult<SubTaskDTO> addSub(@RequestBody SubTaskDTO dto) {
        return ResponseResult.success(todoService.addSubTask(dto));
    }

    @Operation(summary = "更新子项目")
    @PostMapping("sub/update")
    public ResponseResult<SubTaskDTO> updateSub(@RequestBody SubTaskDTO dto) {
        return ResponseResult.success(todoService.updateSubTask(dto));
    }

    @Operation(summary = "删除子项目")
    @PostMapping("sub/delete")
    public ResponseResult<Boolean> deleteSub(@RequestParam("subTaskId") Long subTaskId) {
        return ResponseResult.success(todoService.deleteSubTask(subTaskId));
    }
}
