package com.wanghui.shiyue.article.entity.dto;

import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @fileName: CommentDTO
 * @author: wanghui
 * @createAt: 2024/02/19 03:53:02
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
@ApiModel("文章评论类")
public class CommentDTO extends BaseDTO {
    @ApiModelProperty("评论id")
    private Long commentId;

    @ApiModelProperty("文章id")
    private Long articleId;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("子评论")
    private List<CommentDTO> childComments;
}
