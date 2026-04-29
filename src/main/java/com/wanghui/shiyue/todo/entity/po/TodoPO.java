package com.wanghui.shiyue.todo.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("web.todo")
public class TodoPO extends BasePO {
    @TableId
    @Schema($schema = "待办id")
    private Long todoId;

    @Schema($schema = "标题")
    private String title;

    @Schema($schema = "备注")
    private String remark;

    @Schema($schema = "截止时间")
    private java.util.Date dueTime;

    @Schema($schema = "优先级")
    private Integer priority;

    @Schema($schema = "完成状态")
    private Boolean completed;
}