package com.wanghui.shiyue.article.entity.dto;

import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章与目录关联关系DTO
 * @description: 用于在各层之间传递文章与目录的关联关系数据
 * @fileName: ArticleCatalogDTO
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
public class ArticleCatalogDTO extends BaseDTO {
    @Schema($schema = "关联id")
    private Long articleCatalogId;

    @Schema($schema = "文章id")
    private Long articleId;

    @Schema($schema = "目录id")
    private Long catalogId;

    @Schema($schema = "目录名称")
    private String catalogName;
}
