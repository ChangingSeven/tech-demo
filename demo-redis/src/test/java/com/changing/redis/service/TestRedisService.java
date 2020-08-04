package com.changing.redis.service;

import com.changing.redis.TestBaseService;
import com.changing.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class TestRedisService extends TestBaseService {

    @Resource
    private RedisService redisService;

    @Test
    public void putToHash() {

        redisService.putToHash("TFBANK", "addr", "锦江区下东大街");
        redisService.putToHash("TFBANK", "name", "天府银行");
    }

    @Test
    public void getAllFromHash() {
        Map<String, String> tfthInfo = redisService.getAllFromHash("TFBANK");
        Optional.ofNullable(tfthInfo).get().forEach((k, v) -> {
            log.info("TFBANK:" + k + ":" + v);
        });
    }
}