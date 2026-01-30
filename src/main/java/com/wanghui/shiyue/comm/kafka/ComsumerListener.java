package com.wanghui.shiyue.comm.kafka;


public interface ComsumerListener<T> {

    String topic();

    boolean recieve(T message);
}