package com.changing.websocket.server.websocket;

import com.changing.websocket.server.storage.GlobalDataStorage;
import com.changing.websocket.server.thread.SendDataThread;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 17:38
 * <p>
 * ServerEndpoint 这个注解有什么作用？
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 */

@Slf4j
@Component
@ServerEndpoint("/websocket/{name}")
public class WebSocketServer {

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用户名
     */
    private String name;

    private String clientId;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "name") String name) {
        this.session = session;
        this.name = name;
        String clientId = session.getId();
        this.clientId = clientId;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        GlobalDataStorage.getWebSocketSet().put(clientId, this);

        SendDataThread sendDataThread = new SendDataThread(clientId);
        sendDataThread.start();
        GlobalDataStorage.getThreadMap().put(clientId, sendDataThread);

        log.info("[WebSocket] 连接成功，当前连接人数为：={}", GlobalDataStorage.getWebSocketSet().size());
    }

    @OnClose
    public void onClose() {
        SendDataThread sendDataThread = GlobalDataStorage.getThreadMap().get(this.clientId);
        sendDataThread.interrupt();
        while (!sendDataThread.isInterrupted()) {
            sendDataThread.interrupt();
        }
        log.info("成功关闭线程{}", sendDataThread.getName());

        GlobalDataStorage.getWebSocketSet().remove(this.clientId);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", GlobalDataStorage.getWebSocketSet().size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("[WebSocket] 收到消息：{}", message);
        //判断是否需要指定发送，具体规则自定义
        if (message.indexOf("TOUSER") == 0) {
            String name = message.substring(message.indexOf("TOUSER") + 6, message.indexOf(";"));
            appointSending(name, message.substring(message.indexOf(";") + 1));
        } else {
            groupSending(message);
        }

    }

    /**
     * 群发
     *
     * @param message
     */
    public void groupSending(String message) {
        for (String name : GlobalDataStorage.getWebSocketSet().keySet()) {
            try {
                GlobalDataStorage.getWebSocketSet().get(name).session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定发送
     *
     * @param name
     * @param message
     */
    public void appointSending(String name, String message) {
        try {
            GlobalDataStorage.getWebSocketSet().get(name).session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }

    public String getName() {
        return name;
    }

    public String getClientId() {
        return clientId;
    }

}
