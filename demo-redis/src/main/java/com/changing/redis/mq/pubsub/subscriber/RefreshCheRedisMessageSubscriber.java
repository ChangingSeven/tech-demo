package com.changing.redis.mq.pubsub.subscriber;

import com.changing.redis.mq.pubsub.anotation.RedisSubscriberMethod;
import com.changing.redis.mq.pubsub.anotation.RedisSubscriberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

@RedisSubscriberType
public class RefreshCheRedisMessageSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshCheRedisMessageSubscriber.class);

    private CountDownLatch latch;

    @Autowired
    public RefreshCheRedisMessageSubscriber(CountDownLatch latch) {
        this.latch = latch;
    }

    /**
     * 队列消息接收方法
     *
     * @param jsonMsg 接收到的json格式数据
     * @see org.springframework.data.redis.listener.adapter.MessageListenerAdapter
     */
    @RedisSubscriberMethod(topic = "refreshCache")
    public void refreshCacheMessage(String jsonMsg) {
        LOGGER.info("[开始消费刷新缓存消息队列数据...]");
        try {
            System.out.println(jsonMsg);
            LOGGER.info("[消费刷新缓存消息队列数据成功.]");
        } catch (Exception e) {
            LOGGER.error("[消费刷新缓存消息队列数据失败，失败信息:{}]", e.getMessage());
        }
        latch.countDown();
    }

}