package com.changing.websocket.client.service.impl;

import com.changing.websocket.client.service.WebSocketService;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 17:45
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private WebSocketClient webSocketClient;

    @Override
    public void groupSending(String message) {

        webSocketClient.send(message + "?");
    }

    @Override
    public void appointSending(String name, String message) {

        webSocketClient.send("TOUSER" + name + ";" + message);
    }

}