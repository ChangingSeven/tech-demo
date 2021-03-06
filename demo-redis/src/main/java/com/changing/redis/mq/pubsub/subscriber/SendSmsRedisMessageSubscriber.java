package com.changing.redis.mq.pubsub.subscriber;

import com.changing.redis.mq.pubsub.anotation.RedisSubscriberMethod;
import com.changing.redis.mq.pubsub.anotation.RedisSubscriberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 发送短信通知
 */
@RedisSubscriberType
public class SendSmsRedisMessageSubscriber {
    private static final Logger log = LoggerFactory.getLogger(SendSmsRedisMessageSubscriber.class);

    /**
     * 程序计数器
     * 此属性名只允许为 countDownLatch
     */
    private CountDownLatch countDownLatch;

    public SendSmsRedisMessageSubscriber(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    /**
     * 队列消息接收方法
     *
     * @param jsonMsg
     */
    @RedisSubscriberMethod(topic = "smsMessage")
    public void sendSmsMessage(String jsonMsg) {
        log.info("[开始消费发送短信消息队列sendSmsMessage数据...]");
        try {
            System.out.println(jsonMsg);
            log.info("[消费发送短信消息队列sendSmsMessage数据成功.]");
        } catch (Exception e) {
            log.error("[消费发送短信消息队列sendSmsMessage数据失败，失败信息:{}]", e.getMessage());
        }
        countDownLatch.countDown();
    }

}