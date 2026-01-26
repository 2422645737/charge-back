package com.wanghui.shiyue.comm.kafka.comsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ComsumerListener {

    @KafkaListener(topics = "test-topic",groupId = "test-group")
    public void listen(String message){
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("Received message: " + message);
    }

}