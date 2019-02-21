package org.seefly.springwebsocket.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liujianxin
 * @date 2019-02-15 15:38
 */
@Component
@Slf4j
public class SocketHandle implements WebSocketHandler {

    /**所有已连接的用户会话信息*/
    private final ConcurrentHashMap<String,WebSocketSession> userMap = new ConcurrentHashMap<>();


    /**
     * 连接成功后执行
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("用户{}已连接",session.getId());
        userMap.put(session.getId(),session);
        // 广播新用户上线
        sendMsgForAll(session.getId()+"上线");
    }

    /**
     * 服务端收到消息的处理方式
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        // 推送聊天信息
        if(message instanceof TextMessage){
            TextMessage msg = (TextMessage)message;
            sendMsgForAll(msg.getPayload());
        }
    }

    /**
     * 连接发生异常时的处理方式
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        userMap.remove(session.getId());
        log.info("用户"+session.getId()+"异常下线！");
        // 广播用户下线消息
    }

    /**
     * 任何一方断开连接后处理方式
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        userMap.remove(session.getId());
        // 广播用户下线
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    private void sendMsgForAll(String msg) throws IOException {
        for(WebSocketSession socketSession : userMap.values()){
            socketSession.sendMessage(new TextMessage(msg));
        }
    }
}
