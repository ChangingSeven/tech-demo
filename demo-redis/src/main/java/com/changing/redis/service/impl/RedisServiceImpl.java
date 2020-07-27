package com.changing.redis.service.impl;

import com.changing.redis.service.RedisService;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-07-27 20:48
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addStringKey(String key, Object value) {

        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Integer appendToStringKey(String key, String value) {

        return redisTemplate.opsForValue().append(key, value);
    }

    @Override
    public void addAndExpireStringKey(String key, Object value, long times, TimeUnit timeUnit) {

        redisTemplate.opsForValue().set(key, value, times, timeUnit);
    }

    @Override
    public <T> T getByStringKey(String key, Class<T> t) {

        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean delKey(String key) {

        return redisTemplate.delete(key);
    }

    @Override
    public Boolean expireKey(String key, long times, TimeUnit timeUnit) {

        return redisTemplate.expire(key, times, timeUnit);
    }

    @Override
    public Boolean expireKeyAtTime(String key, Date date) {

        return redisTemplate.expireAt(key, date);
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        if (null == timeUnit) {
            // 默认时间单位：毫秒
            return redisTemplate.getExpire(key);
        } else {
            return redisTemplate.getExpire(key, timeUnit);
        }
    }

    @Override
    public <T> T increment(String key, T value, Class<T> t) throws Exception {
        if (value instanceof Long) {
            return (T) redisTemplate.opsForValue().increment(key, (Long) value);
        } else if (value instanceof Double) {
            return (T) redisTemplate.opsForValue().increment(key, (Double) value);
        } else {
            throw new Exception("不允许的数据类型");
        }
    }

    @Override
    public Long size(String key) {

        return redisTemplate.opsForValue().size(key);
    }
}