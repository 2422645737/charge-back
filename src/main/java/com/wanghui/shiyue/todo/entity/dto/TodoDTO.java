package com.wanghui.shiyue.todo.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanghui.shiyue.comm.entity.BaseDTO;
import com.wanghui.shiyue.todo.entity.enums.TodoPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class TodoDTO extends BaseDTO {
    @Schema($schema = "待办id")
    private Long todoId;

    @Schema($schema = "标题")
    private String title;

    @Schema($schema = "备注")
    private String remark;

    @Schema($schema = "截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueTime;

    @Schema($schema = "优先级 3高 2中 1低")
    private Integer priority;

    @Schema($schema = "完成状态")
    private Boolean completed;

    @Schema($schema = "子项目列表")
    private List<SubTaskDTO> subTasks;
}
