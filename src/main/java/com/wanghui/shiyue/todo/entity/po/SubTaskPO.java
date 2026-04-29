package com.wanghui.shiyue.todo.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("web.todo_subtask")
public class SubTaskPO extends BasePO {
    @TableId
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
}