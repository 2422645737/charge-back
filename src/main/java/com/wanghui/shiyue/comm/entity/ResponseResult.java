package com.wanghui.shiyue.comm.entity;

import com.wanghui.shiyue.comm.codes.BaseCode;
import com.wanghui.shiyue.comm.inter.IErrorCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 通用返回结果包装类
 * @fileName: ResponseResult
 * @author: wanghui
 * @createAt: 2024/02/19 10:45:41
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
public class ResponseResult<T> implements Serializable {
    @ApiModelProperty("标记是否成功")
    private boolean success;

    @ApiModelProperty("错误码")
    private String code;

    @ApiModelProperty("错误信息")
    private String message;

    @ApiModelProperty("返回对象")
    private T data;

    @ApiModelProperty("跟踪ID")
    private String traceId;

    @ApiModelProperty("异常名称")
    private String exceptionName;

    public boolean isSuccess(){
        return this.success;
    }


    public static <T> ResponseResult<T> error(){
        return build(BaseCode.SYSTEM_ERROR,null);
    }

    public static <T> ResponseResult<T> error(IErrorCode iErrorCode){
        return build(iErrorCode,null);
    }
    public static <T> ResponseResult<T> error(IErrorCode iErrorCode,T data){
        return build(iErrorCode,data);
    }
    public static <T> ResponseResult<T> success(T data){
        return build(BaseCode.SUCCESS,data);
    }

    public static <T> ResponseResult<T> success(){
        return build(BaseCode.SUCCESS,null);
    }

    private static <T> ResponseResult build(IErrorCode code, T data){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(code.getCode().equals(BaseCode.SUCCESS.getCode()));
        responseResult.setCode(code.getCode());
        responseResult.setMessage(code.getMsg());
        responseResult.setData(data);
        return responseResult;
    }
}
