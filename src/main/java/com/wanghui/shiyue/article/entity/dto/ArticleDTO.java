package com.wanghui.shiyue.article.entity.dto;

import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @fileName: ArticleDTO
 * @author: wanghui
 * @createAt: 2024/01/12 08:51:09
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
@ApiModel("文章类")
public class ArticleDTO extends BaseDTO {
    @ApiModelProperty("文章id")
    private Long articleId;

    @ApiModelProperty("目录id")
    private Long catalogId;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("标签信息")
    private List<TagDTO> tagList;
}
