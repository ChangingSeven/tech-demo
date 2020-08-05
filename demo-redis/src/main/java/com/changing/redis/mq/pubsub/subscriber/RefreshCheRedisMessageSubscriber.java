package com.changing.redis.mq.pubsub.subscriber;

import com.alibaba.fastjson.JSON;
import com.changing.redis.model.bo.RefreshCacheBO;
import com.changing.redis.mq.pubsub.anotation.RedisSubscriberMethod;
import com.changing.redis.mq.pubsub.anotation.RedisSubscriberType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@RedisSubscriberType
@Slf4j
public class RefreshCheRedisMessageSubscriber {

    /**
     * 程序计数器
     * 此属性名只允许为 countDownLatch
     */
    private CountDownLatch countDownLatch;

    public RefreshCheRedisMessageSubscriber(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    /**
     * 队列消息接收方法
     *
     * @param jsonMsg 接收到的json格式数据
     * @see org.springframework.data.redis.listener.adapter.MessageListenerAdapter
     */
    @RedisSubscriberMethod(topic = "refreshCache")
    public void refreshCacheMessage(String jsonMsg) {
        log.info("[开始消费刷新缓存消息队列数据...]");
        try {
            RefreshCacheBO refreshCacheBO = JSON.parseObject(jsonMsg, RefreshCacheBO.class);
            String keyType = refreshCacheBO.getKeyType();
            String keyName = refreshCacheBO.getKeyName();
            String keyValue = refreshCacheBO.getKeyValue();
            log.info("消费刷新缓存消息队列数据成功, 字典类型为：{}, 字典-键为：{}, 字典-值为：{}", keyType, keyName, keyValue);
        } catch (Exception e) {
            log.error("[消费刷新缓存消息队列数据失败，失败信息:{}]", e.getMessage());
        }
        countDownLatch.countDown();
    }

}