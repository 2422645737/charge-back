package com.wanghui.shiyue.todo.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class TodoQueryParam {
    @Schema($schema = "完成状态")
    private Boolean completed;

    @Schema($schema = "优先级")
    private Integer priority;

    @Schema($schema = "开始时间")
    private Date startTime;

    @Schema($schema = "结束时间")
    private Date endTime;

    @Schema($schema = "是否按优先级降序")
    private Boolean orderByPriority;
}
