package com.changing.websocket.client.configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 18:41
 */
@Slf4j
@Component
public class ScanClientSchedule implements ApplicationRunner {

    @Autowired
    private WebSocketClient webSocketClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(this::scanClient, 0, 5, TimeUnit.SECONDS);
    }

    public void scanClient() {

        WebSocket.READYSTATE readyState = webSocketClient.getReadyState();
        if (WebSocket.READYSTATE.CLOSED.equals(readyState)
            || WebSocket.READYSTATE.NOT_YET_CONNECTED.equals(readyState)) {

            try {
                boolean b = webSocketClient.connectBlocking();
                System.out.println("重连结果：" + b);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}