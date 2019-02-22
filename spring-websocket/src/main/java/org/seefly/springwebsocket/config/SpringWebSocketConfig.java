package org.seefly.springwebsocket.config;

import org.seefly.springwebsocket.handle.SpringWebSocketHandle;
import org.seefly.springwebsocket.handle.SocketHandle;
import org.seefly.springwebsocket.interceptor.HandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * Spring框架实现WebSocket
 *
 * doc: https://docs.spring.io/spring/docs/5.0.7.RELEASE/spring-framework-reference/web.html#websocket
 *
 * @author liujianxin
 * @date 2019-02-20 09:54
 */
@EnableWebSocket
@Configuration
public class SpringWebSocketConfig implements WebSocketConfigurer {
    private HandshakeInterceptor interceptor;
    private SocketHandle handle;
    private SpringWebSocketHandle springWebSocketHandle;

    public SpringWebSocketConfig(HandshakeInterceptor interceptor, SocketHandle handle, SpringWebSocketHandle springWebSocketHandle){
        this.interceptor = interceptor;
        this.springWebSocketHandle = springWebSocketHandle;
        this.handle = handle;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(springWebSocketHandle,"/audio").setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024 * 1024);
        container.setMaxBinaryMessageBufferSize(1024 * 1024);
        return container;
    }
}
