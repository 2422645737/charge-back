package com.wanghui.shiyue.todo.entity.dto;

import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SubTaskDTO extends BaseDTO {
    @Schema($schema = "子项目id")
    private Long subTaskId;

    @Schema($schema = "所属待办id")
    private Long todoId;

    @Schema($schema = "父子项目id")
    private Long parentId;

    @Schema($schema = "子项目名称")
    private String name;

    @Schema($schema = "完成状态")
    private Boolean completed;

    @Schema($schema = "子项目子集")
    private List<SubTaskDTO> children;
}
