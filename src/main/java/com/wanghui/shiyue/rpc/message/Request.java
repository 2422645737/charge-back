package com.wanghui.shiyue.rpc.message;


import lombok.Data;

@Data
public class Request {

    private String serviceName;

    private String methodName;

    private String[] parameterTypes;
    private Object[] params;
}