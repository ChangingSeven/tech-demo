package com.changing.springbatch.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-11-17 14:55
 */
@RestController
@RequestMapping("/server")
public class ServerController {

    @RequestMapping("/health")
    public Map health() {
        Map<String, String> map = new HashMap<>();

        map.put("status", "UP");
        map.put("serverTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        return map;
    }

    @RequestMapping("/info")
    public Map info() {
        Map<String, String> map = new HashMap<>();

        map.put("ip", "192.168.8.50");
        map.put("port", "9080");
        map.put("serverTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        return map;
    }

}