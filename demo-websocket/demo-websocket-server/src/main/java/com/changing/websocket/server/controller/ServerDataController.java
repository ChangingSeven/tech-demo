package com.changing.websocket.server.controller;

import com.changing.websocket.server.storage.GlobalDataStorage;
import com.changing.websocket.server.thread.SendDataThread;
import com.changing.websocket.server.websocket.WebSocketServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 18:08
 */
@Slf4j
@RestController
@RequestMapping("/server")
public class ServerDataController {

    @RequestMapping("/clientInfo")
    public Map clientInfo() {
        Map<String, Object> result = new HashMap<>();

        ConcurrentHashMap<String, WebSocketServer> webSocketSet = GlobalDataStorage.getWebSocketSet();
        for (Map.Entry<String, WebSocketServer> entry : webSocketSet.entrySet()) {
            WebSocketServer webSocketServer = entry.getValue();
            String name = webSocketServer.getName();
            Session session = webSocketServer.getSession();
            String clientId = session.getId();
            String rawPath = session.getRequestURI().getRawPath();
            Map<String, String> pathParameters = session.getPathParameters();

            result.put("name", name);
            result.put("clientId", clientId);
            result.put("rawPath", rawPath);
            result.put("pathParameters", pathParameters);
            break;
        }

        return result;
    }

    @RequestMapping("/switchClientState")
    public Map switchClientState() {

        Map<String, Object> result = new HashMap<>();

        ConcurrentHashMap<String, WebSocketServer> webSocketSet = GlobalDataStorage.getWebSocketSet();
        try {
            for (Map.Entry<String, WebSocketServer> entry : webSocketSet.entrySet()) {
                String clientId = entry.getKey();
                SendDataThread sendDataThread = GlobalDataStorage.getThreadMap().get(clientId);
                if (null != sendDataThread) {
                    sendDataThread.setRunning(!sendDataThread.getRunning());

                    result.put("clientId", clientId);
                    result.put("runningState", sendDataThread.getRunning());
                    log.info("切换状态的客户端ID为: {}, 切换后状态：{}", clientId, sendDataThread.getRunning());
                }

                break;
            }
        } catch (Exception e) {
            log.error("更改线程执行状态异常", e);
        }

        return result;
    }

}