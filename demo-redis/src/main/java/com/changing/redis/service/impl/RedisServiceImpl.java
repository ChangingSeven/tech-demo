package com.changing.redis.service.impl;

import com.changing.redis.service.RedisService;

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
    public void addAndExpireStringKey(String key, Object value, long times, TimeUnit timeUnit) {

        redisTemplate.opsForValue().set(key, value, times, timeUnit);
    }

    @Override
    public <T> T getByStringKey(String key, Class<T> t) {

        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean delStringKey(String key) {

        return redisTemplate.delete(key);
    }

}