package org.seefly.springwebsocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 *
 * /socket端口
 *  https://docs.spring.io/spring/docs/5.0.7.RELEASE/spring-framework-reference/web.html#websocket-fallback
 * 用来模拟使用webSocket实时的推送公告
 * @author liujianxin
 * @date 2019-02-15 13:59
 */
@Controller
public class StompSocketController {
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * 用来接受客户端发送的webSocket请求
     * 管理员使用webSocket请求该接口(/app/change-notice)，然后将管理员发送的信息推送到所有订阅/topic/notice的客户端
     */
    @MessageMapping("/change-notice")
    public void greeting(String value){
        System.out.println(value);
        // 使用‘MessageConverter’进行包装转化成一条消息，发送到指定的目标
        this.simpMessagingTemplate.convertAndSend("/topic/notice", value);
    }

    @MessageMapping("/bytes")
    public void binary(byte[] bytes){
        String s = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(s);
    }

    @MessageMapping("/audioMessage")
    public void byteArray(byte[] data){
            System.out.println(data.length);
    }

    /**
     * 另一种方式完成同样的功能
     */
    @MessageMapping("/change-notice2")
    @SendTo("/topic/notice")
    public String greeting2(String value) {
        return value;
    }
}
