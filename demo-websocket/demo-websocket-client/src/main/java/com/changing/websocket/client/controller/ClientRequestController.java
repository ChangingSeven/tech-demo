package com.changing.websocket.client.controller;

import com.changing.websocket.client.service.WebSocketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 17:49
 */
@RestController
@RequestMapping("/client")
public class ClientRequestController {

    @Autowired
    private WebSocketService webSocketService;

    @RequestMapping("/sendMessage")
    public String sendMessage(String message) {

        webSocketService.groupSending(message);

        return message;
    }

}