package com.wanghui.shiyue.comm.redis.component;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Resource
    RedisTemplate<String,String> redisTemplate;


    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }

    public void set(String key,String value,long timeout){
        redisTemplate.opsForValue().set(key,value,timeout);
    }

    public void set(String key, String value, long timeout, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }


    public void delete(String key){
        redisTemplate.delete(key);
    }

    public boolean exists(String key){
        return redisTemplate.hasKey(key);
    }

}