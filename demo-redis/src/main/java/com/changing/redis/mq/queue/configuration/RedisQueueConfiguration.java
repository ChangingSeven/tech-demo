package com.changing.redis.mq.queue.configuration;

import com.changing.redis.mq.queue.QueueConfiguration;
import com.changing.redis.mq.queue.producer.QueueSender;
import com.changing.redis.mq.queue.RedisMqConsumerContainer;
import com.changing.redis.mq.queue.consumer.TestListener;
import com.changing.redis.service.RedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-08-05 23:29
 */
@Configuration
public class RedisQueueConfiguration {

    /**
     * 配置redis消息队列消费者容器
     *
     * @param redisService redis
     * @return 消费者容器
     */
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public RedisMqConsumerContainer redisMqConsumerContainer(@Autowired RedisService redisService) {
        RedisMqConsumerContainer config = new RedisMqConsumerContainer(redisService);
        config.addConsumer(QueueConfiguration.builder().queue("TEST").consumer(new TestListener()).build());
        return config;
    }

    /**
     * 配置redis消息队列生产者
     *
     * @param redisService redis
     * @return 生产者
     */
    @Bean
    public QueueSender queueSender(@Autowired RedisService redisService) {
        return new QueueSender(redisService);
    }
}