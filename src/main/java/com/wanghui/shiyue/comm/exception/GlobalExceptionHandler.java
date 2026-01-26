package com.wanghui.shiyue.comm.exception;

import com.wanghui.shiyue.comm.codes.BaseCode;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @description: 捕获和处理系统中的各种异常，提供统一的错误反馈
 * @fileName: GlobalExceptionHandler
 * @author: wanghui
 * @createAt: 2026/01/25 10:00:00
 * @updateBy:
 * @copyright: 众阳健康
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理参数验证异常
     * @param e IllegalArgumentException
     * @return ResponseResult
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseResult<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数验证失败: {}", e.getMessage());
        return ResponseResult.error(BaseCode.SYSTEM_ERROR, e.getMessage());
    }

    /**
     * 处理运行时异常
     * @param e RuntimeException
     * @return ResponseResult
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<?> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage(), e);
        return ResponseResult.error(BaseCode.SYSTEM_ERROR, "系统运行时异常: " + e.getMessage());
    }

    /**
     * 处理所有异常
     * @param e Exception
     * @return ResponseResult
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseResult.error(BaseCode.SYSTEM_ERROR, "系统异常: " + e.getMessage());
    }
}
