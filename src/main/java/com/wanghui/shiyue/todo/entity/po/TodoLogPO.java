package com.wanghui.shiyue.todo.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("web.todo_log")
public class TodoLogPO extends BasePO {
    @TableId
    private Long logId;

    @Schema($schema = "操作类型")
    private String action;

    @Schema($schema = "操作对象id")
    private Long targetId;

    @Schema($schema = "操作内容")
    private String content;

    @Schema($schema = "操作结果")
    private String result;
}