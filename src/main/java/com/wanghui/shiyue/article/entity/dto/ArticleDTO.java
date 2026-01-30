package com.wanghui.shiyue.article.entity.dto;

import com.wanghui.shiyue.comm.entity.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @fileName: ArticleDTO
 * @author: wanghui
 * @createAt: 2024/01/12 08:51:09
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
public class ArticleDTO extends BaseDTO {
    @Schema($schema = "文章id")
    private Long articleId;

    @Schema($schema = "父目录id")
    private Long catalogId;

    @Schema($schema = "文章标题")
    private String title;

    @Schema($schema = "文章内容")
    private String content;

    @Schema($schema = "文章状态 0：草稿   1：已保存")
    private Integer status;

    @Schema($schema = "标签信息")
    private List<TagDTO> tagList;

    @Schema($schema = "文章类型信息")
    private Long classId;

    @Schema($schema = "摘要")
    private String summary;

    @Schema($schema = "文章封面地址")
    private String cover;

    @Schema($schema = "作废标识")
    private String invalidFlag;

    @Schema($schema = "目录名称")
    private String catalogName;

    @Schema($schema = "目录信息")
    private CatalogDTO catalog;
}