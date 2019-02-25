package org.seefly.springwebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

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
 * StompSubProtocolHandler
 * @author liujianxin
 * @date 2019-02-15 13:55
 */
@Configuration
@EnableWebSocketMessageBroker
public class SpringStompConfig extends AbstractWebSocketMessageBrokerConfigurer {




    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new ByteArrayMessageConverter());
        return true;
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
