package com.wanghui.shiyue.article.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

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
@TableName("tag")
public class TagPO extends BasePO {

    @ApiModelProperty("标签id")
    @TableId
    private Long tagId;

    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("作废标识")
    private String invalidFlag;

    @ApiModelProperty("标签下文章数量")
    @TableField(exist = false)
    private Integer nums;
    @Override
    public void init() {
        this.invalidFlag = NumberUtils.INTEGER_ZERO.toString();
        super.init();
    }
}
