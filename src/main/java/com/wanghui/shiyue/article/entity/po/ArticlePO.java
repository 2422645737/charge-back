package com.wanghui.shiyue.article.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @fileName: ArticlePO
 * @author: wanghui
 * @createAt: 2024/01/13 07:18:50
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
@TableName(value = "article")
public class ArticlePO extends BasePO {
    @TableId
    @Schema($schema = "文章id")
    private Long articleId;

    @Schema($schema = "目录id")
    private Long catalogId;

    @Schema($schema = "文章标题")
    private String title;

    @Schema($schema = "文章内容")
    private String content;

    @Schema($schema = "文章类型信息")
    private Long classId;

    @Schema($schema = "摘要")
    private String summary;

    @Schema($schema = "文章封面地址")
    private String cover;

    @Schema($schema = "作废标识")
    private String invalidFlag;
}