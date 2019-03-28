package org.seefly.springwebsocket.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stomp事件的监听
 * 1、从何处发生？
 *
 * 可监听的事件见：{@link org.springframework.web.socket.messaging}
 *
 * @author liujianxin
 * @date 2019-02-26 16:06
 */
@Slf4j
@Component
public class StompEventListener {
    @Resource(name = "websocketContextSessionHolder")
    private ConcurrentHashMap<String,String> websocketContextSessionHolder;

    /**
     * 链接成功后的事件
     * 存储客户端标识、用于向指定用户发送信息
     */
    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent event) {
        Principal user = event.getUser();
        log.info("WebSocket新连接建立[{}]",user.getName());
        websocketContextSessionHolder.put(user.getName(),"-1");
    }

    /**
     * 监听WebSocket断开链接事件
     */
    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event){
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = sha.getUser();
        websocketContextSessionHolder.remove(user.getName());
        log.info("WebSocket连接断开[{}]",user.getName());
    }
}
