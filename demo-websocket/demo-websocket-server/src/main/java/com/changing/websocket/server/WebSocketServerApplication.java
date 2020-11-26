package com.changing.websocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-03-17 13:18
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class WebSocketServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketServerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebSocketServerApplication.class);
    }

}