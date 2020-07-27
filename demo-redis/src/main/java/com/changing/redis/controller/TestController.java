package com.changing.redis.controller;

import com.changing.redis.service.RedisService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    /**
     *
     * redis数据类型及使用场景：https://www.cnblogs.com/superfj/p/9232482.html
     * redis原生方法：https://www.runoob.com/redis/redis-strings.html
     * redis方法示例：https://www.cnblogs.com/KIV-Y/p/10763213.html
     *
     */

    @Autowired
    private RedisService redisService;

    @GetMapping("/time")
    public String test01() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        String format = simpleDateFormat.format(date);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "changing");
        map.put("age", 22);
        redisService.addAndExpireStringKey("test-key", map, 10, TimeUnit.SECONDS);

        Map<String, Object> value = redisService.getByStringKey("test-key", Map.class);
        log.info("key: test-time, value: " + value.keySet());

        log.info("time requested: " + format);

        return "Current Time Is: " + format;
    }

}