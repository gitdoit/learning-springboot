package org.seefly.springwebsocket.context;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket会话控制
 * @author liujianxin
 * @date 2019-02-22 17:47
 */
public class WebSocketSessionHolder {
    private static final ConcurrentHashMap<String, WebSocketSession> CONTEXT = new ConcurrentHashMap<>();

    public boolean putSession(String sessionId,WebSocketSession socketSession){
        return  CONTEXT.put(sessionId,socketSession) != null;
    }

    public WebSocketSession getSession(String sessionId){
        return CONTEXT.get(sessionId);
    }

    public boolean remove(String sessionId){
        return CONTEXT.remove(sessionId) != null;
    }
}
