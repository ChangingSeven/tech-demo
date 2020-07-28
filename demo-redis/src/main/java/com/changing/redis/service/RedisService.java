package com.changing.redis.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-07-27 20:48
 */
public interface RedisService {

    /**
     * 删除单个键值对
     *
     * @param key 键
     * @return 删除结果
     */
    Boolean delKey(String key);

    /**
     * 设置键的失效时间
     *
     * @param key      键
     * @param times    时长
     * @param timeUnit 时间单位
     * @return 失效操作结果
     */
    Boolean expireKey(String key, long times, TimeUnit timeUnit);

    /**
     * 在指定时间失效键
     *
     * @param key  键
     * @param date 日期
     * @return 失效操作结果
     */
    Boolean expireKeyAtTime(String key, Date date);

    /**
     * 获取键的剩余失效时长，指定时间单位
     *
     * @param key      键
     * @param timeUnit 时间单位
     * @return 剩余失效时长
     */
    Long getExpire(String key, TimeUnit timeUnit);

    /**
     * 获取键所对应的值的数据类型
     *
     * @param key 键
     * @return 值的数据类型
     */
    String getDataType(String key);

    /**
     * 插入新的键值对
     *
     * @param key   键
     * @param value 值
     */
    void addStringKey(String key, Object value);

    /**
     * 在某个键的值后面连接新的字符串
     *
     * @param key   键
     * @param value 值
     * @return 连接新字符串之后，当前值的总长度
     */
    Integer appendToStringKey(String key, String value);

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
     * 增长键的值
     *
     * @param key   键
     * @param value 增长值
     * @param t     值
     * @param <T>   指定类型
     * @return 增长后的值
     * @throws Exception 异常
     */
    <T> T increment(String key, T value, Class<T> t) throws Exception;

    /**
     * 获取值的长度
     *
     * @param key 键
     * @return 值的长度
     */
    Long getValueLengthByStringKey(String key);

    /**
     * 从列表左侧插入新元素
     *
     * @param key   键
     * @param value 值
     * @return 列表当前长度
     */
    Long listLeftPush(String key, String value);

    /**
     * 从列表右侧移除值
     *
     * @param key 键
     * @return 移除元素的值
     */
    <T> T listRightPop(String key, Class<T> t);

    /**
     * 从列表右侧插入新元素
     *
     * @param key   键
     * @param value 值
     * @return 列表当前长度
     */
    Long listRightPush(String key, String value);

    /**
     * 从列表左侧移除值
     *
     * @param key 键
     * @return 移除元素的值
     */
    <T> T listLeftPop(String key, Class<T> t);

    /**
     * 在超时时间内从最左侧移除一个元素，并返回被移除元素的值
     * @param key 键
     * @param times 时长
     * @param timeUnit 时间单位
     * @param t 返回值
     * @param <T> 返回值类型
     * @return 返回值
     */
    <T> T listBLeftPop(String key, long times, TimeUnit timeUnit, Class<T> t);

}