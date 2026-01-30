package com.wanghui.shiyue.article.entity.enums;

public enum ArticleStatus {
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布");

    private final Integer code;
    private final String desc;

    ArticleStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ArticleStatus of(int code) {
        for (ArticleStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的文章状态 code：" + code);
    }
}