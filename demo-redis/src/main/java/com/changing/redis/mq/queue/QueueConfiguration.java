package com.changing.redis.mq.queue;

import com.changing.redis.mq.queue.consumer.MsgConsumer;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-08-05 23:21
 */
public class QueueConfiguration {
    /**
     * 队列名称
     */
    private String queue;
    /**
     * 消费者
     */
    private MsgConsumer consumer;

    private QueueConfiguration() {
    }

    public static Builder builder() {
        return new Builder();
    }

    String getQueue() {
        return queue;
    }

    MsgConsumer getConsumer() {
        return consumer;
    }

    public static class Builder {
        private QueueConfiguration configuration = new QueueConfiguration();

        public QueueConfiguration defaultConfiguration(MsgConsumer consumer) {
            configuration.consumer = consumer;
            configuration.queue = consumer.getClass().getSimpleName();
            return configuration;
        }

        public Builder queue(String queue) {
            configuration.queue = queue;
            return this;
        }

        public Builder consumer(MsgConsumer consumer) {
            configuration.consumer = consumer;
            return this;
        }

        public QueueConfiguration build() {
            if (configuration.queue == null || configuration.queue.length() == 0) {
                if (configuration.consumer != null) {
                    configuration.queue = configuration.getClass().getSimpleName();
                }
            }
            return configuration;
        }

    }
}