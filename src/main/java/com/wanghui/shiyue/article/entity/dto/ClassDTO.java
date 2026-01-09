package com.wanghui.shiyue.article.entity.dto;

import lombok.Data;

@Data
public class ClassDTO {
    private Long classId;

    private String className;

    private String invalidFlag;

    /**
     * 每种类型文章的数量
     */
    private Integer count;
}