package com.changing.redis.mq.pubsub.publisher.impl;

import com.changing.redis.mq.pubsub.publisher.CommonPublisher;
import com.changing.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonPublisherImpl implements CommonPublisher {

    @Autowired
    private RedisService redisService;

    @Override
    public <T> void send(String channel, T data, Class<T> t) {

        redisService.sendDataToChannel(channel, data, t);
    }

}