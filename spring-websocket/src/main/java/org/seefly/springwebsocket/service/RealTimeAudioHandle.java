package org.seefly.springwebsocket.service;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.nio.ByteBuffer;

/**
 * @author liujianxin
 * @date 2019-02-20 15:04
 */
@Component
public class RealTimeAudioHandle implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if(message instanceof BinaryMessage){
            BinaryMessage bm =  (BinaryMessage)message;
            ByteBuffer payload = bm.getPayload();
            byte[] array = payload.array();
            System.out.println(array.length);
        }else if (message instanceof TextMessage){
            TextMessage bm =  (TextMessage)message;
            System.out.println(bm.getPayload());
        }else {
            System.out.println(message.getClass());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
