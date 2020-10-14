package com.changing.websocket.server.storage;

import com.changing.websocket.server.thread.SendDataThread;
import com.changing.websocket.server.websocket.WebSocketServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-29 09:33
 */
public class GlobalDataStorage {

    private static Map<String, SendDataThread> threadMap = new ConcurrentHashMap<>();

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();

    public static Map<String, SendDataThread> getThreadMap() {
        return threadMap;
    }

    public static ConcurrentHashMap<String, WebSocketServer> getWebSocketSet() {

        return webSocketSet;
    }
}