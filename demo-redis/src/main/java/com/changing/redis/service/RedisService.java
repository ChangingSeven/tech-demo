package com.changing.redis.service;

import com.changing.redis.exception.UnSupportRedisDataTypeException;

import java.util.Collection;
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
    <T> T increment(String key, T value, Class<T> t) throws UnSupportRedisDataTypeException;

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
     *
     * @param key      键
     * @param times    时长
     * @param timeUnit 时间单位
     * @param t        返回值
     * @param <T>      返回值类型
     * @return 返回值
     */
    <T> T listBLeftPop(String key, long times, TimeUnit timeUnit, Class<T> t);

    /**
     * 向无序集合添加新元素
     *
     * @param key    键
     * @param values 值
     * @return 集合中元素个数
     */
    Long addToSet(String key, Object... values);

    /**
     * 从无序集合中移除指定数量个元素
     *
     * @param key     键
     * @param itemNum 移除元素数量
     * @return 被移除元素
     */
    List<Object> popFromSet(String key, long itemNum);

    /**
     * 获取无序集合中元素数量
     *
     * @param key 键
     * @return 集合大小
     */
    Long sizeOfSet(String key);

    /**
     * 获取无序集合中所有元素
     *
     * @param key 键
     * @param t   类型
     * @param <T> 对象类型
     * @return 集合元素
     */
    <T> Set<T> membersOfSet(String key, Class<T> t);

    /**
     * 获取第一个无序集合在集合2中不存在的元素
     *
     * @param key  被比较的集合的键
     * @param key2 集合2的键
     * @param t    类型
     * @param <T>  对象类型
     * @return 第一个集合中与其它集合不相同的元素集合
     */
    <T> Set<T> differenceOfSet(String key, String key2, Class<T> t);

    /**
     * 获取两个无序集合的交集
     *
     * @param key  键1
     * @param key2 键2
     * @param t    类型
     * @param <T>  对象类型
     * @return 相同元素的集合
     */
    <T> Set<T> intersectSet(String key, String key2, Class<T> t);

    /**
     * 获取两个无序集合的并集
     *
     * @param key  键1
     * @param key2 键2
     * @param t    类型
     * @param <T>  对象类型
     * @return 并集集合
     */
    <T> Set<T> unionSet(String key, String key2, Class<T> t);

    /**
     * 根据无序集合中元素产生随机数
     *
     * @param key 键
     * @param t   类型
     * @param <T> 对象类型
     * @return 随机数集合
     */
    <T> T randomFromSet(String key, Class<T> t);

    /**
     * 移除无序集合中指定值的元素
     *
     * @param key    键
     * @param t      类型
     * @param values 指定的值
     * @param <T>    对象类型
     * @return 被移除数量
     */
    <T> Long removeFromSetByValue(String key, Class<T> t, T... values);

    /**
     * 添加元素到有序集合中
     *
     * @param key   键
     * @param score 分值
     * @param t     类型
     * @param value 值
     * @param <T>   对象类型
     */
    <T> Boolean addToZSet(String key, double score, T value, Class<T> t);

    /**
     * 获取有序集合的元素统计数
     *
     * @param key 键
     * @return 元素统计数
     */
    Long sizeOfZSet(String key);

    /**
     * 获取有序集合中分值满足要求的元素统计数
     *
     * @param key 键
     * @param min 分值最小值
     * @param max 分值最大值
     * @return 元素统计数
     */
    Long sizeOfZSetByScore(String key, double min, double max);

    /**
     * 根据下标索引获取有序集合中的元素
     *
     * @param key   键
     * @param start 起始下标
     * @param end   结束下标
     * @param t     类型
     * @param <V>   对象类型
     * @return 元素集合
     */
    <V> Set<V> rangeByIndex(String key, long start, long end, Class<V> t);

    /**
     * 获取有序集合中某个元素的下标索引
     *
     * @param key   键
     * @param value 值
     * @param t     类型
     * @param <V>   对象类型
     * @return 元素下标
     */
    <V> Long rankOfZSetByValue(String key, V value, Class<V> t);

    /**
     * 根据元素的值移除有序集合中的元素
     *
     * @param key    键
     * @param t      类型
     * @param values 值
     * @param <V>    对象类型
     * @return 集合长度
     */
    <V> Long removeFromZSetByValues(String key, Class<V> t, V... values);

    /**
     * 获取有序集合中元素对应的分值
     *
     * @param key   键
     * @param value 值
     * @return 元素的分值
     */
    <V> Double getScoreOfZSetByValue(String key, V value, Class<V> t);

    /**
     * 新增元素到Hash集合中
     *
     * @param key   键
     * @param filed 属性名
     * @param value 值
     * @param <F>   属性类型
     * @param <V>   值类型
     */
    <F, V> void putToHash(String key, F filed, V value);

    /**
     * 当属性在Hash集合中不存在时，才做插入操作
     *
     * @param key   键
     * @param filed 属性名
     * @param value 值
     * @param <F>   属性名类型
     * @param <V>   属性值
     */
    <F, V> void putToHashIfAbsent(String key, F filed, V value);

    /**
     * 新增多个元素到Hash集合中
     *
     * @param key  键
     * @param data 元素集合
     * @param <F>  键类型
     * @param <V>  值类型
     */
    <F, V> void putToHash(String key, Map<F, V> data);

    /**
     * 根据属性名移除值
     *
     * @param key   键
     * @param t     属性名类型
     * @param filed 属性名
     * @param <T>   属性名类型
     * @return 被移除元素数量
     */
    <T> Long deleteFromSetByFiled(String key, Class<T> t, T... filed);

    /**
     * 校验属性名在Hash集合中是否存在
     *
     * @param key   键
     * @param filed 属性名
     * @param t     类型
     * @param <T>   对象类型
     * @return 存在与否
     */
    <T> Boolean checkFiledExistInHashKey(String key, T filed, Class<T> t);

    /**
     * 根据属性名获取Hash集合中的值
     *
     * @param key   键
     * @param filed 属性名
     * @param <F>   属性类型
     * @param <V>   值类型
     * @param t     返回值类型
     * @return 属性名对应的值
     */
    <F, V> V getValueByFiledFromHash(String key, F filed, Class<V> t);

    /**
     * 根据多个属性名获取Hash集合中的值
     *
     * @param key   键
     * @param filed 属性名集合
     * @param <F>   属性类型
     * @param <V>   值类型
     * @return 获取到的元素集合
     */
    <F, V> List<V> getValueByMultiFiledFromHash(String key, Collection<F> filed);

    /**
     * 获取Hash中的所有数据
     *
     * @param key 键
     * @param <F> 属性名类型
     * @param <V> 值类型
     * @return 数据集合
     */
    <F, V> Map<F, V> getAllFromHash(String key);

    /**
     * 获取Hash集合中所有的属性名
     *
     * @param key 键
     * @param <F> 属性名类型
     * @return 属性名集合
     */
    <F> Set<F> getAllFiledFromHash(String key);

    /**
     * 获取Hash集合中所有的值
     *
     * @param key 键
     * @param <V> 值类型
     * @return 值的集合
     */
    <V> List<V> getAllValueFromHash(String key);

    /**
     * 获取Hash集合中元素的数量
     *
     * @param key 键
     * @return 元素的数量
     */
    Long lengthOfHash(String key);
}