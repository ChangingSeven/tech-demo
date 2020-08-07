package com.changing.redis.mq.queue;

import com.changing.redis.mq.queue.consumer.MsgConsumer;
import com.changing.redis.service.RedisService;

import java.util.concurrent.TimeUnit;

import org.springframework.dao.QueryTimeoutException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-08-05 23:20
 */
@Slf4j
public class QueueListener implements Runnable {

    private RedisService redisService;
    private String queue;
    private MsgConsumer consumer;

    public QueueListener(RedisService redisService, String queue, MsgConsumer consumer) {
        this.redisService = redisService;
        this.queue = queue;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        log.info("QueueListener start...queue:{}", queue);
        while (RedisMqConsumerContainer.run) {
            try {
                String msg = redisService.listBLeftPop(queue, 30, TimeUnit.SECONDS, String.class);
                if (msg != null) {
                    try {
                        consumer.onMessage(msg);
                    } catch (Exception e) {
                        consumer.onError(msg, e);
                    }
                }
            } catch (QueryTimeoutException ignored) {
                log.info("队列请求超时...");
            } catch (Exception e) {
                if (RedisMqConsumerContainer.run) {
                    log.error("Queue:{}", queue, e);
                } else {
                    log.info("QueueListener exits...queue:{}", queue);
                }
            }
        }
    }

}