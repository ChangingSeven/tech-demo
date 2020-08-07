package com.changing.redis.mq.queue.consumer;

import com.changing.redis.mq.queue.consumer.MsgConsumer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-08-05 23:27
 */
@Slf4j
public class TestListener implements MsgConsumer {

    @Override
    public void onMessage(Object message) {
        log.info("收到消息:" + message);
    }

    @Override
    public void onError(Object msg, Exception e) {
        log.error("发生错误,消息:{}", msg, e);
    }

}