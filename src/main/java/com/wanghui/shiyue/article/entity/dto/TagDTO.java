package com.wanghui.shiyue.article.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class TagDTO extends BaseDTO {

    @Schema($schema = "标签id")
    private Long tagId;

    @Schema($schema = "标签名称")
    private String tagName;

    @Schema($schema = "作废标识")
    private String invalidFlag;

    @Schema($schema = "标签下文章数量")
    private Integer nums;
}