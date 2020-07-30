package com.changing.redis.service.impl;

import com.changing.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-07-27 20:48
 */
@Service
public class RedisServiceImpl implements RedisService {

    /**
     * 所有键的统一前缀
     */
    public static final String REDIS_KEY_PREFIX = "rdsk_";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Boolean delKey(String key) {

        return redisTemplate.delete(REDIS_KEY_PREFIX + key);
    }

    @Override
    public Boolean expireKey(String key, long times, TimeUnit timeUnit) {

        return redisTemplate.expire(REDIS_KEY_PREFIX + key, times, timeUnit);
    }

    @Override
    public Boolean expireKeyAtTime(String key, Date date) {

        return redisTemplate.expireAt(REDIS_KEY_PREFIX + key, date);
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        if (null == timeUnit) {
            // 默认时间单位：毫秒
            return redisTemplate.getExpire(REDIS_KEY_PREFIX + key);
        } else {
            return redisTemplate.getExpire(REDIS_KEY_PREFIX + key, timeUnit);
        }
    }

    @Override
    public String getDataType(String key) {
        DataType dataType = redisTemplate.type(REDIS_KEY_PREFIX + key);

        return dataType.code();
    }

    @Override
    public void addStringKey(String key, Object value) {

        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public Integer appendToStringKey(String key, String value) {

        return redisTemplate.opsForValue().append(REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public void addAndExpireStringKey(String key, Object value, long times, TimeUnit timeUnit) {

        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + key, value, times, timeUnit);
    }

    @Override
    public <T> T getByStringKey(String key, Class<T> t) {

        return (T) redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + key);
    }

    @Override
    public <T> T increment(String key, T value, Class<T> t) throws Exception {
        if (value instanceof Long) {
            return (T) redisTemplate.opsForValue().increment(REDIS_KEY_PREFIX + key, (Long) value);
        } else if (value instanceof Double) {
            return (T) redisTemplate.opsForValue().increment(REDIS_KEY_PREFIX + key, (Double) value);
        } else {
            throw new Exception("不允许的数据类型");
        }
    }

    @Override
    public Long getValueLengthByStringKey(String key) {

        return redisTemplate.opsForValue().size(REDIS_KEY_PREFIX + key);
    }

    @Override
    public Long listLeftPush(String key, String value) {

        return redisTemplate.opsForList().leftPush(REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public <T> T listRightPop(String key, Class<T> t) {

        return (T) redisTemplate.opsForList().rightPop(REDIS_KEY_PREFIX + key);
    }

    @Override
    public Long listRightPush(String key, String value) {

        return redisTemplate.opsForList().rightPush(REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public <T> T listLeftPop(String key, Class<T> t) {

        return (T) redisTemplate.opsForList().leftPop(REDIS_KEY_PREFIX + key);
    }

    @Override
    public <T> T listBLeftPop(String key, long times, TimeUnit timeUnit, Class<T> t) {

        return (T) redisTemplate.opsForList().leftPop(REDIS_KEY_PREFIX + key, times, timeUnit);
    }

    @Override
    public Long addToSet(String key, Object... values) {

        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public List<Object> popFromSet(String key, long itemNum) {
        if (itemNum < 1) {
            return null;
        }

        if (1 == itemNum) {
            Object popItem = redisTemplate.opsForSet().pop(key);
            return Collections.singletonList(popItem);
        } else {
            return redisTemplate.opsForSet().pop(key, itemNum);
        }
    }

    @Override
    public Long sizeOfSet(String key) {

        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public <T> Set<T> membersOfSet(String key, Class<T> t) {

        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public <T> Set<T> differenceOfSet(String key, String key2, Class<T> t) {

        return redisTemplate.opsForSet().difference(key, key2);
    }

    @Override
    public <T> Set<T> intersectSet(String key, String key2, Class<T> t) {

        return redisTemplate.opsForSet().intersect(key, key2);
    }

    @Override
    public <T> Set<T> unionSet(String key, String key2, Class<T> t) {

        return redisTemplate.opsForSet().union(key, key2);
    }

    @Override
    public <T> T randomFromSet(String key, Class<T> t) {

        return (T) redisTemplate.opsForSet().randomMember(key);
    }

    @Override
    public <T> Long removeFromSetByValue(String key, Class<T> t, T... values) {

        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public <T> Boolean addToZSet(String key, double score, T value, Class<T> t) {

        return redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Long sizeOfZSet(String key) {

        return redisTemplate.opsForZSet().size(key);
    }

    public void set() {


    }

}