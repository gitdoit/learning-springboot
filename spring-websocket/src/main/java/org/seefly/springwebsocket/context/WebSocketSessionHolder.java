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

    public static boolean putSession(String sessionId,WebSocketSession socketSession){
        return  CONTEXT.put(sessionId,socketSession) != null;
    }

    public static WebSocketSession getSession(String sessionId){
        return CONTEXT.get(sessionId);
    }

    public static boolean remove(String sessionId){
        return CONTEXT.remove(sessionId) != null;
    }

    public static WebSocketSession getAny(){
        return CONTEXT.entrySet().stream().findAny().get().getValue();
    }
}
