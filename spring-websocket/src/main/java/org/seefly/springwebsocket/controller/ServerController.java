package org.seefly.springwebsocket.controller;

import org.seefly.springwebsocket.context.WebSocketSessionHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Principal;
import java.util.Base64;

/**
 * @author liujianxin
 * @date 2019-02-25 18:12
 */
@RequestMapping("/send")
@RestController
public class ServerController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;



    @GetMapping("/{msg}")
    public String getMessage(@PathVariable("msg") String mes) throws IOException {
        WebSocketSessionHolder.getAny().sendMessage(new TextMessage(mes));
        return "OK";
    }

    @GetMapping("/audio/stream")
    public String sendStream() throws Exception {
        // 音频转base64
        RandomAccessFile aFile = new RandomAccessFile("E:\\voice\\konghao\\lian_tong_kong_hao.wav", "rw");
        FileChannel channel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int)aFile.length());
        channel.read(buf);
        channel.close();
        buf.flip();

        String s = Base64.getEncoder().encodeToString(buf.array());
        // 发送
        this.simpMessagingTemplate.convertAndSendToUser(WebSocketSessionHolder.id,"/audio/stream",s);
        return "OK";
    }
}
