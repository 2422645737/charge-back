package com.wanghui.shiyue.comm.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @fileName: SwaggerProperties
 * @author: wanghui
 * @createAt: 2024/01/12 01:58:38
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    /**
     * 标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;

    /**
     * 项目路径
     */
    private String termsOfServiceUrl;

    /**
     * 作者
     */
    private String authorName;

    /**
     * 邮箱
     */
    private String authorEmail;

    /**
     * 作者主页
     */
    private String authorUrl;

    /**
     * 版本
     */
    private String version;

    /**
     * 扫描的路径
     */
    private String basePackage;

}
