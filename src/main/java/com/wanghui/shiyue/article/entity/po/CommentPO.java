package com.wanghui.shiyue.article.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BaseDTO;
import com.wanghui.shiyue.comm.entity.BasePO;
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
@TableName("comment")
public class CommentPO extends BasePO {
    @TableId
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
    private List<CommentPO> childComments;
}