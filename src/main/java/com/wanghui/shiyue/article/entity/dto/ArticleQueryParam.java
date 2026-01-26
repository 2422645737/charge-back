package com.wanghui.shiyue.article.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleQueryParam {

    private Long classId;

    private List<Long> tagIds;

    private Long articleID;
    
    private Long catalogId;
    
    private List<Long> catalogIds;
    
    private Integer catalogLevel;
    
    private String catalogPath;
}