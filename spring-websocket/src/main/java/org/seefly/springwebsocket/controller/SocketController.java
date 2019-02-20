package org.seefly.springwebsocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 用来模拟使用webSocket实时的推送公告
 * @author liujianxin
 * @date 2019-02-15 13:59
 */
@Controller
public class SocketController {
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping("/helloSocket")
    public String index(){
        return "/index";
    }

    @RequestMapping("/client")
    public String client(){
        return "/client";
    }

    @RequestMapping("socket")
    public String socket(){
        return "socket";
    }

    @RequestMapping("/real")
    public String realTimeAudio(){
        return "/realtime";
    }

    /**
     * 用来接受客户端发送的webSocket请求
     * 管理员使用webSocket请求该接口(/app/change-notice)，然后将管理员发送的信息推送到所有订阅/topic/notice的客户端
     */
    @MessageMapping("/change-notice")
    public void greeting(String value){
        // 使用‘MessageConverter’进行包装转化成一条消息，发送到指定的目标
        this.simpMessagingTemplate.convertAndSend("/topic/notice", value);
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