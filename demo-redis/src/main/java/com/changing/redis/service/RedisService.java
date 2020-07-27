package com.changing.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-07-27 20:48
 */
public interface RedisService {

    /**
     * 插入新的键值对
     *
     * @param key   键
     * @param value 值
     */
    void addStringKey(String key, Object value);

    /**
     * 插入新的键值对并设置失效时间
     *
     * @param key      键
     * @param value    值
     * @param times    时长
     * @param timeUnit 时间单位
     */
    void addAndExpireStringKey(String key, Object value, long times, TimeUnit timeUnit);

    /**
     * 根据键获取值
     *
     * @param key 键
     * @param t   返回类型
     * @param <T> 范型
     * @return 获取到的数据对象
     */
    <T> T getByStringKey(String key, Class<T> t);

    /**
     * 删除单个键值对
     *
     * @param key 键
     * @return 删除结果
     */
    Boolean delStringKey(String key);

}