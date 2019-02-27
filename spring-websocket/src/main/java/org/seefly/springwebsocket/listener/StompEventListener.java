package org.seefly.springwebsocket.listener;

import org.seefly.springwebsocket.context.WebSocketSessionHolder;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;

/**
 * Stomp事件的监听
 * 1、从何处发生？
 *
 * 可监听的事件见：{@link org.springframework.web.socket.messaging}
 *
 * @author liujianxin
 * @date 2019-02-26 16:06
 */
@Component
public class StompEventListener {
    /**
     * 链接成功后的事件
     */
    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent event) {
        // Get Accessor
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = event.getUser();
        MessageHeaders headers = event.getMessage().getHeaders();
        MessageHeaders messageHeaders = sha.getMessageHeaders();

        WebSocketSessionHolder.id = user.getName();
    }
}
