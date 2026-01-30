package com.wanghui.shiyue.comm.kafka;


public interface Producer<T> {

    /**
     * @param topic
     * @param data
     */

    void send(String topic, T data);
}