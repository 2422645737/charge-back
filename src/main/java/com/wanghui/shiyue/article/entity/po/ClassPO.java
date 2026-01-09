package com.wanghui.shiyue.article.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ClassPO {

    @TableId
    private Long classId;

    private String className;

    private String invalidFlag;
}