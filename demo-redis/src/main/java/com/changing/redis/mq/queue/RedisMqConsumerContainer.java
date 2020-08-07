package com.changing.redis.mq.queue;

import com.changing.redis.service.RedisService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-08-05 23:22
 */
@Slf4j
public class RedisMqConsumerContainer {

    private Map<String, QueueConfiguration> consumerMap = new HashMap<>();
    private RedisService redisService;
    static boolean run;
    private ExecutorService exec;

    public RedisMqConsumerContainer(RedisService redisService) {
        this.redisService = redisService;
    }

    public void addConsumer(QueueConfiguration configuration) {
        if (consumerMap.containsKey(configuration.getQueue())) {
            log.warn("Key:{} this key already exists, and it will be replaced", configuration.getQueue());
        }
        if (configuration.getConsumer() == null) {
            log.warn("Key:{} consumer cannot be null, this configuration will be skipped", configuration.getQueue());
        }
        consumerMap.put(configuration.getQueue(), configuration);
    }

    public void destroy() {
        run = false;
        this.exec.shutdown();
        log.info("QueueListener exiting.");
        while (!this.exec.isTerminated()) {

        }
        log.info("QueueListener exited.");
    }

    public void init() {
        run = true;
        this.exec = Executors.newCachedThreadPool(r -> {
            final AtomicInteger threadNumber = new AtomicInteger(1);
            return new Thread(r, "RedisMQListener-" + threadNumber.getAndIncrement());
        });
        consumerMap = Collections.unmodifiableMap(consumerMap);
        consumerMap.forEach((k, v) -> exec.submit(new QueueListener(redisService, v.getQueue(), v.getConsumer())));
    }
}