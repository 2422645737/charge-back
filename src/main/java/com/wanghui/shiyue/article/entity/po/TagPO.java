package com.wanghui.shiyue.article.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
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
@TableName("tag")
public class TagPO extends BasePO {

    @Schema($schema = "标签id")
    @TableId
    private Long tagId;

    @Schema($schema = "标签名称")
    private String tagName;

    @Schema($schema = "作废标识")
    private String invalidFlag;

    @Schema($schema = "标签下文章数量")
    @TableField(exist = false)
    private Integer nums;
    @Override
    public void init() {
        this.invalidFlag = NumberUtils.INTEGER_ZERO.toString();
        super.init();
    }
}