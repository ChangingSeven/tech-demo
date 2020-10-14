package com.changing.websocket.client.configuration;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-09-28 17:41
 */
@Slf4j
@Component
public class WebSocketConfiguration {

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            String connURI = "ws://localhost:8085/websocket/test";
            WebSocketClient webSocketClient = new WebSocketClient(new URI(connURI), new Draft_6455()) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[websocket] 连接成功");
                }

                @Override
                public void onMessage(String message) {

                    log.info("[websocket] 收到消息={}", message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[websocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[websocket] 连接错误={}", ex.getMessage());
                }

                @Override
                public void sendPing() throws NotYetConnectedException {
                    super.sendPing();
                }

                @Override
                public void onWebsocketPing(WebSocket conn, Framedata f) {
                    super.onWebsocketPing(conn, f);

                    String payloadData = new String(f.getPayloadData().asCharBuffer().array());
                    log.info("收到服务端发来的心跳请求, payloadData: " + payloadData);
                }

                @Override
                public void onWebsocketPong(WebSocket conn, Framedata f) {
                    super.onWebsocketPong(conn, f);

                    String payloadData = new String(f.getPayloadData().asCharBuffer().array());
                    log.info("收到服务端发来的心跳响应, payloadData: " + payloadData);
                }
            };
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            log.error("create websocket client failed", e);
        }

        return null;
    }

}