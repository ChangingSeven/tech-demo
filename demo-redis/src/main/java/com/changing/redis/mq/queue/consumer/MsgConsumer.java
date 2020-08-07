package com.changing.redis.mq.queue.consumer;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-08-05 23:19
 */
public interface MsgConsumer {

    void onMessage(Object message);

    void onError(Object msg, Exception e);

}