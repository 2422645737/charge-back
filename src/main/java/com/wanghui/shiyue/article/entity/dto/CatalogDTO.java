package com.wanghui.shiyue.article.entity.dto;

import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 目录层级结构DTO
 * @description: 用于在各层之间传递目录数据，支持目录树状结构的展示
 * @fileName: CatalogDTO
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
public class CatalogDTO extends BaseDTO {
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

    @Schema($schema = "子目录列表")
    private List<CatalogDTO> children;
}
