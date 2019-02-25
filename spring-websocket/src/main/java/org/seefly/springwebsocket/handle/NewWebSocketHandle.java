package org.seefly.springwebsocket.handle;

import lombok.extern.slf4j.Slf4j;
import org.seefly.springwebsocket.context.WebSocketSessionHolder;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.nio.ByteBuffer;

/**
 * @author liujianxin
 * @date 2019-02-25 17:58
 */
@Slf4j
public class NewWebSocketHandle extends AbstractWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketSessionHolder.putSession(session.getId(),session);
        log.info("新会话加入:"+session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSessionHolder.remove(session.getId());
        log.info("会话关闭:"+session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println(payload);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer payload = message.getPayload();
        byte[] array = payload.array();
        System.out.println("Audio_length:"+array.length);
    }

}
