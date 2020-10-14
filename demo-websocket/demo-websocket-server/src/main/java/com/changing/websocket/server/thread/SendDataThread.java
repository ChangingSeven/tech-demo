package com.changing.websocket.server.thread;

import com.changing.websocket.server.storage.GlobalDataStorage;
import com.changing.websocket.server.websocket.WebSocketServer;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 18:52
 */
@Slf4j
public class SendDataThread extends Thread {

    private String clientId;

    private Boolean isRunning = true;

    public SendDataThread(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void run() {

        while (true) {

            if (isRunning) {
                Random random = new SecureRandom();
                int nextInt = random.nextInt(100);

                ConcurrentHashMap<String, WebSocketServer> webSocketSet = GlobalDataStorage.getWebSocketSet();
                WebSocketServer webSocketServer = webSocketSet.get(this.clientId);
                if (null != webSocketServer) {
                    webSocketServer.groupSending("randomNum=" + nextInt);
                    try {
                        Thread.sleep(4000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String threadName = Thread.currentThread().getName();
            log.info("当前线程{}运行中...", threadName);
        }

    }

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        this.isRunning = running;
    }

}