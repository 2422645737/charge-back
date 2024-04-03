package com.wanghui.shiyue.article.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 文章标签
 * @fileName: TagDTO
 * @author: wanghui
 * @createAt: 2024/01/12 03:10:18
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
@ApiModel("文章tag")
public class TagDTO extends BaseDTO {

    @ApiModelProperty("标签id")
    private Long tagId;

    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("作废标识")
    private String invalidFlag;

    @ApiModelProperty("标签下文章数量")
    private Integer nums;
}
