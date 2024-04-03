package com.wanghui.shiyue.comm.codes;

import com.wanghui.shiyue.comm.inter.IErrorCode;

/**
 * @description: 基础返回编码
 * @fileName: BaseCode
 * @author: wanghui
 * @createAt: 2024/02/19 10:50:56
 * @updateBy:
 * @copyright: 众阳健康
 */
public enum BaseCode implements IErrorCode {
    /**
     *
     */
    SUCCESS("0000", "成功"),

    FALLBACK("system@9998", "系统错误,$1"),
    SYSTEM_ERROR("system@9999", "系统繁忙"),

    TAG_REPEAT("0001","标签已存在");

    private String code;
    private String msg;
    private BaseCode(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String prefix() {
        return "";
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
