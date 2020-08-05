package com.changing.redis.mq.pubsub.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.concurrent.CountDownLatch;

@Configuration
public class MessageListenerContainerConfiguration {
    /**
     * template注入的地方
     *
     * @see com.changing.redis.configuration.RedisConfiguration
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 创建监听容器
     *
     * @return 监听容器实体bean
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        return container;
    }

    /**
     * 计数器，用来控制线程
     *
     * @return 计数器bean
     */
    @Bean
    public CountDownLatch countDownLatch() {
        //指定了计数的次数 1
        return new CountDownLatch(1);
    }
}