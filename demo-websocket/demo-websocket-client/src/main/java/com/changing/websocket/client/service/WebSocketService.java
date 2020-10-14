package com.changing.websocket.client.service;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 17:44
 */
public interface WebSocketService {

    /**
     * 群发
     *
     * @param message
     */
    void groupSending(String message);

    /**
     * 指定发送
     *
     * @param name
     * @param message
     */
    void appointSending(String name, String message);

}