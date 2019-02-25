package org.seefly.springwebsocket.handle;

import org.seefly.springwebsocket.context.WebSocketSessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author liujianxin
 * @date 2019-02-20 15:04
 */
@Component
public class SpringWebSocketHandle implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketSessionHolder.putSession(session.getId(),session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if(message instanceof BinaryMessage){
            BinaryMessage bm =  (BinaryMessage)message;
            ByteBuffer payload = bm.getPayload();
            byte[] array = payload.array();
            File file = new File("D:\\audio"+System.currentTimeMillis()+".wav");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(array);
            fileOutputStream.close();
            System.out.println(array.length);
        }else if (message instanceof TextMessage){
            TextMessage bm =  (TextMessage)message;
            if("play".equals(bm.getPayload())){
                System.out.println(bm.getPayload());
                RandomAccessFile aFile = new RandomAccessFile("E:\\voice\\konghao\\lian_tong_kong_hao.wav", "rw");
                FileChannel channel = aFile.getChannel();
                ByteBuffer buf = ByteBuffer.allocate((int)aFile.length());
                channel.read(buf);
                channel.close();
                buf.flip();
                BinaryMessage msg = new BinaryMessage(buf);
                session.sendMessage(msg);
            }

        }else {
            System.out.println(message.getClass());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
