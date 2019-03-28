package org.seefly.springwebsocket.controller;

import org.seefly.springwebsocket.model.SessionData;
import org.seefly.springwebsocket.model.WebSocketRequest;
import org.seefly.springwebsocket.model.WebSocketResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 官方文档：
 *      https://docs.spring.io/spring/docs/5.0.7.RELEASE/spring-framework-reference/web.html#websocket-fallback
 * 注解说明:
 *      @Payload : 用于MessageMapping标注的方法的参数中，用于提取负载中的参数转换到指定类型。
 *                  如果只有一个参数，则不需要标注即默认使用
 *      @Header  ：用于获取标头信息
 *      @Headers ：用于获取所有标头
 *      @DestinationVariable ： 用于获取目的地变量
 *  参数说明：
 *      Message ： 完整消息
 *      MessageHeaders ： 标头
 *      MessageHeaderAccessor，SimpMessageHeaderAccessor，StompHeaderAccessor ： 。。。
 *      java.security.Principal ： 用户认证信息
 * 用来模拟使用webSocket实时的推送公告
 * @author liujianxin
 * @date 2019-02-15 13:59
 */
@Controller
public class StompSocketController {

    /**客户端订阅地址*/
    private static final String CLIENT_SUBSCRIBE = "/message/sub";
    @Resource(name = "websocketContextSessionHolder")
    private ConcurrentHashMap<String,String> websocketContextSessionHolder;
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;
    private static String TEXT = "/audio/text";
    private static String AUDIO = "/audio/stream";


    /**
     * 接收二进制信息，需要配置该类型参数的消息转换器，默认已经配好了
     */
    @MessageMapping("/bytes")
    public void binary(byte[] bytes){
        String s = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(s);
    }

    /**
     *  1、接收自定义消息 ByteBuffer,需要自定义消息转换器
     *  2、单点发送convertAndSendToUser，客户端需要订阅 /user/../..，然后在发送的时候指定用户名称
     *      这个用户名称需要手动配置，即在握手阶段配置
     */
    @MessageMapping("/audioStream")
    public void byteArray(ByteBuffer data, Principal principal){
        System.out.println(data.array().length);
        this.simpMessagingTemplate.convertAndSendToUser(principal.getName(),TEXT,"from Server=本次录音共:"+data.array().length+"字节");
    }

    @MessageMapping("/object")
    public void get(SessionData data){
        System.out.println(data);
    }


    @MessageMapping("/order")
    public void init(WebSocketRequest request, Principal principal){
        // 初始化模板
        if("init".equals(request.getType())){
            WebSocketResponse response;
            String robotId = request.getRobotId();
            if(StringUtils.isEmpty(robotId)){
                response =  new WebSocketResponse().setType("exception").setContent("话术模板不能为空！").setStatus("10001");
            }else {
                response = new WebSocketResponse().setType("init").setStatus("200");
            }
            simpMessagingTemplate.convertAndSendToUser(principal.getName(),CLIENT_SUBSCRIBE,response);
        }
        // 建立WebSocket和语音网关的会话映射
        else if("sessionId".equals(request.getType())){
            websocketContextSessionHolder.put(principal.getName(),request.getSessionId());
        }
        //TODO 去获取训练结果
        else if("end".equals(request.getType())){

        }else {
            WebSocketResponse response = new WebSocketResponse().setType("exception").setContent("未知命令类型！").setStatus("1000");
            simpMessagingTemplate.convertAndSendToUser(principal.getName(),CLIENT_SUBSCRIBE,response);
        }
    }

}
