package com.changing.redis.mq.pubsub.publisher;

public interface CommonPublisher {

    /**
     * 发送数据到指定的通道内
     *
     * @param channel 通道名
     * @param data    数据
     * @param t       数据类型
     * @param <T>     数据对象类型
     */
    <T> void send(String channel, T data, Class<T> t);

}