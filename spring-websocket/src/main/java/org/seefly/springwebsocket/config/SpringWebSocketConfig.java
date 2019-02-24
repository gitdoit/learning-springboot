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
import org.springframework.web.socket.server.support.AbstractHandshakeHandler;

/**
 * Spring框架实现WebSocket
 *
 * doc: https://docs.spring.io/spring/docs/5.0.7.RELEASE/spring-framework-reference/web.html#websocket
 *
 * 在处理握手的时候，在{@link AbstractHandshakeHandler#doHandshake}的281行，会调用一个认证信息方法。
 * 我觉得Http转WebSocket的用户信息可以通过这种方式来做，把Http中的认证信息放到WebSocketSession中就很Nice。
 * 我们实现一下这个抽象类，替代原有默认的
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

        // HttpSessionHandshakeInterceptor 用来在握手时将HttpSession 中的属性复制到 WebSocketSession
        registry.addHandler(springWebSocketHandle,"/audio").addInterceptors(interceptor).setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024 * 1024);
        container.setMaxBinaryMessageBufferSize(1024 * 1024);
        return container;
    }
}
