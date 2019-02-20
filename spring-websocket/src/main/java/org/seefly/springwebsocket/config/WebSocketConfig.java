package org.seefly.springwebsocket.config;

import org.seefly.springwebsocket.interceptor.HandshakeInterceptor;
import org.seefly.springwebsocket.service.RealTimeAudioHandle;
import org.seefly.springwebsocket.service.SocketHandle;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author liujianxin
 * @date 2019-02-20 09:54
 */
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    private HandshakeInterceptor interceptor;
    private SocketHandle handle;
    private RealTimeAudioHandle realTimeAudioHandle;

    public WebSocketConfig(HandshakeInterceptor interceptor,SocketHandle handle, RealTimeAudioHandle realTimeAudioHandle){
        this.interceptor = interceptor;
        this.realTimeAudioHandle = realTimeAudioHandle;
        this.handle = handle;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handle,"/send").addInterceptors(interceptor).setAllowedOrigins("*");
        registry.addHandler(realTimeAudioHandle,"/audio").setAllowedOrigins("*");
    }
}
