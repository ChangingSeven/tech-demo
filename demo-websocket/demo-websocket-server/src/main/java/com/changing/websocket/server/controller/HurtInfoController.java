package com.changing.websocket.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-23 16:10
 */
@RestController
@RequestMapping("/hurt/info")
public class HurtInfoController {

    @GetMapping("/health")
    public String health() {

        return "server is running...";
    }

}