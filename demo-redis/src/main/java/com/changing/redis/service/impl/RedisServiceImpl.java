package com.changing.redis.service.impl;

import com.changing.redis.constants.CommonConstant;
import com.changing.redis.exception.UnSupportRedisDataTypeException;
import com.changing.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-07-27 20:48
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Boolean delKey(String key) {

        Boolean deleteResult = redisTemplate.delete(CommonConstant.REDIS_KEY_PREFIX + key);
        log.info("删除redis数据结果:{}, key:{}", deleteResult, key);
        return deleteResult;
    }

    @Override
    public Boolean expireKey(String key, long times, TimeUnit timeUnit) {
        Boolean expireResult = redisTemplate.expire(CommonConstant.REDIS_KEY_PREFIX + key, times, timeUnit);
        log.info("设置redis数据失效时长:{}, key:{}, time:{}, timeUnit:{}", expireResult, key, times, timeUnit);
        return expireResult;
    }

    @Override
    public Boolean expireKeyAtTime(String key, Date date) {

        return redisTemplate.expireAt(CommonConstant.REDIS_KEY_PREFIX + key, date);
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        if (null == timeUnit) {
            // 默认时间单位：毫秒
            return redisTemplate.getExpire(CommonConstant.REDIS_KEY_PREFIX + key);
        } else {
            return redisTemplate.getExpire(CommonConstant.REDIS_KEY_PREFIX + key, timeUnit);
        }
    }

    @Override
    public String getDataType(String key) {
        DataType dataType = redisTemplate.type(CommonConstant.REDIS_KEY_PREFIX + key);
        return dataType.code();
    }

    @Override
    public void addStringKey(String key, Object value) {

        redisTemplate.opsForValue().set(CommonConstant.REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public Integer appendToStringKey(String key, String value) {

        return redisTemplate.opsForValue().append(CommonConstant.REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public void addAndExpireStringKey(String key, Object value, long times, TimeUnit timeUnit) {

        redisTemplate.opsForValue().set(CommonConstant.REDIS_KEY_PREFIX + key, value, times, timeUnit);
    }

    @Override
    public <T> T getByStringKey(String key, Class<T> t) {

        return (T) redisTemplate.opsForValue().get(CommonConstant.REDIS_KEY_PREFIX + key);
    }

    @Override
    public <T> T increment(String key, T value, Class<T> t) throws UnSupportRedisDataTypeException {
        if (value instanceof Long) {
            return (T) redisTemplate.opsForValue().increment(CommonConstant.REDIS_KEY_PREFIX + key, (Long) value);
        } else if (value instanceof Double) {
            return (T) redisTemplate.opsForValue().increment(CommonConstant.REDIS_KEY_PREFIX + key, (Double) value);
        } else {
            throw new UnSupportRedisDataTypeException("不允许的数据类型");
        }
    }

    @Override
    public Long getValueLengthByStringKey(String key) {

        return redisTemplate.opsForValue().size(CommonConstant.REDIS_KEY_PREFIX + key);
    }

    @Override
    public Long listLeftPush(String key, String value) {

        return redisTemplate.opsForList().leftPush(CommonConstant.REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public <T> T listRightPop(String key, Class<T> t) {

        return (T) redisTemplate.opsForList().rightPop(CommonConstant.REDIS_KEY_PREFIX + key);
    }

    @Override
    public Long listRightPush(String key, String value) {

        return redisTemplate.opsForList().rightPush(CommonConstant.REDIS_KEY_PREFIX + key, value);
    }

    @Override
    public <T> T listLeftPop(String key, Class<T> t) {

        return (T) redisTemplate.opsForList().leftPop(CommonConstant.REDIS_KEY_PREFIX + key);
    }

    @Override
    public <T> T listBLeftPop(String key, long times, TimeUnit timeUnit, Class<T> t) {

        return (T) redisTemplate.opsForList().leftPop(CommonConstant.REDIS_KEY_PREFIX + key, times, timeUnit);
    }

    @Override
    public Long addToSet(String key, Object... values) {

        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public List<Object> popFromSet(String key, long itemNum) {
        if (itemNum < 1) {
            return Collections.emptyList();
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

    @Override
    public Long sizeOfZSetByScore(String key, double min, double max) {

        return redisTemplate.opsForZSet().count(key, min, max);
    }

    @Override
    public <T> Set<T> rangeByIndex(String key, long start, long end, Class<T> t) {

        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public <T> Long rankOfZSetByValue(String key, T value, Class<T> t) {

        return redisTemplate.opsForZSet().rank(key, value);
    }

    @Override
    public <T> Long removeFromZSetByValues(String key, Class<T> t, T... values) {

        return redisTemplate.opsForZSet().remove(key, values);
    }

    @Override
    public <T> Double getScoreOfZSetByValue(String key, T value, Class<T> t) {

        return redisTemplate.opsForZSet().score(key, value);
    }

    @Override
    public <F, V> void putToHash(String key, F filed, V value) {

        redisTemplate.opsForHash().put(key, filed, value);
    }

    @Override
    public <F, V> void putToHashIfAbsent(String key, F filed, V value) {

        redisTemplate.opsForHash().putIfAbsent(key, filed, value);
    }

    @Override
    public <F, V> void putToHash(String key, Map<F, V> data) {

        redisTemplate.opsForHash().putAll(key, data);
    }

    @Override
    public <T> Long deleteFromSetByFiled(String key, Class<T> t, T... filed) {

        return redisTemplate.opsForHash().delete(key, filed);
    }

    @Override
    public <T> Boolean checkFiledExistInHashKey(String key, T filed, Class<T> t) {

        return redisTemplate.opsForHash().hasKey(key, filed);
    }

    @Override
    public <F, T> T getValueByFiledFromHash(String key, F filed, Class<T> t) {

        return (T) redisTemplate.opsForHash().get(key, filed);
    }

    @Override
    public <F, V> List<V> getValueByMultiFiledFromHash(String key, Collection<F> filed) {

        return redisTemplate.opsForHash().multiGet(key, filed);
    }

    @Override
    public <F, V> Map<F, V> getAllFromHash(String key) {

        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public <F> Set<F> getAllFiledFromHash(String key) {

        return redisTemplate.opsForHash().keys(key);
    }

    @Override
    public <V> List<V> getAllValueFromHash(String key) {

        return redisTemplate.opsForHash().values(key);
    }

    @Override
    public Long lengthOfHash(String key) {

        return redisTemplate.opsForHash().size(key);
    }

}