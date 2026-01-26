package com.wanghui.shiyue.article.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wanghui.shiyue.comm.entity.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章与目录关联关系实体类
 * @description: 对应数据库中的article_catalog表，用于存储文章与目录的多对多关联关系
 * @fileName: ArticleCatalogPO
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
@TableName(value = "article_catalog")
public class ArticleCatalogPO extends BasePO {
    @TableId
    @Schema($schema = "关联id")
    private Long articleCatalogId;

    @Schema($schema = "文章id")
    private Long articleId;

    @Schema($schema = "目录id")
    private Long catalogId;

    @Schema($schema = "目录名称")
    private String catalogName;
}
