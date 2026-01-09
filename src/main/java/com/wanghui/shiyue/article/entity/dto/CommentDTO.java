package com.wanghui.shiyue.article.entity.dto;

import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class CommentDTO extends BaseDTO {
    @Schema($schema = "评论id")
    private Long commentId;

    @Schema($schema = "文章id")
    private Long articleId;

    @Schema($schema = "文章标题")
    private String title;

    @Schema($schema = "评论内容")
    private String content;

    @Schema($schema = "用户id")
    private Long userId;

    @Schema($schema = "用户名")
    private String userName;

    @Schema($schema = "父id")
    private Long parentId;

    @Schema($schema = "子评论")
    private List<CommentDTO> childComments;
}