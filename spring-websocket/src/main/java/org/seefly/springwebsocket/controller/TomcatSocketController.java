package org.seefly.springwebsocket.controller;

import org.springframework.stereotype.Controller;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * javaEE规范，tomcat实现
 *
 * from:
 *  https://www.jianshu.com/p/f93ffc498995
 *
 *  解决依赖注入
 *      https://blog.csdn.net/lotts/article/details/77944672
 *
 *
 * 可以解决二进制录音上传
 * @author liujianxin
 * @date 2019-02-21 13:18
 */
@Controller
@ServerEndpoint(value = "/websocket")
public class TomcatSocketController {
    @OnMessage(maxMessageSize=160000)
    public void onMessage(byte[] message, Session session) {
        System.out.println("消息大小"+message.length);
    }
    @OnClose
    public void onClose(Session session, CloseReason reason) {

    }
}
