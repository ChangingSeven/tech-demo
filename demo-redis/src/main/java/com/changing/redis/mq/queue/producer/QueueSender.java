package com.changing.redis.mq.queue.producer;

import com.changing.redis.service.RedisService;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-08-05 23:17
 */
public class QueueSender {

    private RedisService redisService;

    public QueueSender(RedisService redisService) {
        this.redisService = redisService;
    }

    public void sendMsg(String queue, String msg) {

        redisService.listRightPush(queue, msg);
    }

}