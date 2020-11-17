package com.changing.springbatch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-10-14 14:31
 */
@SpringBootApplication
@MapperScan("com.changing.springbatch.mapper")
public class SpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}