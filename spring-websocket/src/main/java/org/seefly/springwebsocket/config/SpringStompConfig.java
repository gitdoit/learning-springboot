package org.seefly.springwebsocket.config;

import org.apache.catalina.User;
import org.seefly.springwebsocket.converter.CustomMessageConverter;
import org.seefly.springwebsocket.model.MyUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;
import java.util.UUID;

/**
 * stomp和WebSocket的关系
 * https://stackoverflow.com/questions/40988030/what-is-the-difference-between-websocket-and-stomp-protocols
 *
 * stomp简单教程
 * https://www.jianshu.com/p/60799f1356c5
 *
 * stomp官方实例
 * https://spring.io/guides/gs/messaging-stomp-websocket/
 *
 * webstomp.min.js
 *  https://www.jsdelivr.com/package/npm/webstomp-client
 *
 * Stomp单点订阅发布
 * https://stackoverflow.com/questions/37853727/where-user-comes-from-in-convertandsendtouser-works-in-sockjsspring-websocket/47956531
 * https://stackoverflow.com/questions/22367223/sending-message-to-specific-user-on-spring-websocket
 * StompSubProtocolHandler
 * @author liujianxin
 * @date 2019-02-15 13:55
 */
@Configuration
@EnableWebSocketMessageBroker
public class SpringStompConfig extends AbstractWebSocketMessageBrokerConfigurer {


    /**
     * 消息转换
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new CustomMessageConverter());
        return true;
    }

    /**
     * 客户端绑定管道时的操作
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {

            /**
             * 在握手阶段，给每个链接实例分配一个‘用户’
             * 目的是可以在向客户端发送消息时，可以点对点发送。
             * 而客户端的操作就是在订阅指定url的时候前面加一个 /user/ 即可
             *
             * 给每个连接分配一个‘用户’ 的操作也可以通过自定义握手处理器然后覆盖determineUser 方法来实现
             *
             */
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // access authentication header(s)
                    // Authentication user = SecurityContextHolder.getContext().getAuthentication();
                    accessor.setUser(new MyUser(UUID.randomUUID().toString()));
                }
                return message;
            }
        });
    }





    /**
     * 添加一个服务端点，来接收客户端的连接。
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket");
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //表示客户端订阅地址的前缀信息，也就是客户端接收服务端消息的地址的前缀信息
        registry.enableSimpleBroker("/topic");
        //指服务端接收地址的前缀，意思就是说客户端给服务端发消息的地址的前缀
        registry.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(1024*1024);
    }



}
