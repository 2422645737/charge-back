package com.wanghui.shiyue.article.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("文章表实体类")
public class ArticlePO extends BasePO {
    @TableId
    @ApiModelProperty("文章id")
    private Long articleId;

    @ApiModelProperty("目录id")
    private Long catalogId;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章内容")
    private String content;

}
