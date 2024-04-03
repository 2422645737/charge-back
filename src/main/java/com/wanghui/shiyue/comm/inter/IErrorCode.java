package com.wanghui.shiyue.comm.inter;

/**
 * @description:
 * @fileName: IErrorCode
 * @author: wanghui
 * @createAt: 2024/02/19 10:56:39
 * @updateBy:
 * @copyright: 众阳健康
 */
public interface IErrorCode {
    /**
     * 前缀
     * @return {@link String }
     */
    String prefix();

    /**
     * 错误码code
     * @return {@link String }
     */
    String getCode();

    /**
     * 错误消息
     * @return {@link String }
     */
    String getMsg();
}
