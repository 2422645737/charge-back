package com.wanghui.shiyue.comm.kafka.component;

import com.wanghui.shiyue.comm.kafka.Producer;
import jakarta.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer<T> implements Producer<T> {


    @Resource
    private KafkaTemplate<String,T> kafkaTemplate;

    @Override
    public void send(String topic, T data) {
        kafkaTemplate.send(topic,data);
    }
}