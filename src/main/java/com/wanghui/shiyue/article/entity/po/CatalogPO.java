package com.wanghui.shiyue.article.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 目录层级结构实体类
 * @description: 对应数据库中的catalog表，用于存储目录的层级结构信息
 * @fileName: CatalogPO
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
@TableName(value = "catalog")
public class CatalogPO extends BasePO {
    @TableId
    @Schema($schema = "目录id")
    private Long catalogId;

    @Schema($schema = "父目录id，顶级目录为0")
    private Long parentId;

    @Schema($schema = "目录名称")
    private String name;

    @Schema($schema = "目录层级，从1开始")
    private Integer level;

    @Schema($schema = "目录路径，如 \"1/2/3\"")
    private String path;

    @Schema($schema = "作废标识")
    private String invalidFlag;
}
