package org.seefly.springwebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liujianxin
 * @date 2019-02-21 15:02
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("helloSocket").setViewName("index");
        registry.addViewController("client").setViewName("client");
        // spring实现webSocket
        registry.addViewController("spsocket").setViewName("websocket/socket");
        registry.addViewController("real").setViewName("realtime");
        registry.addViewController("stomp").setViewName("stomp/stompaudio");
        registry.addViewController("nstomp").setViewName("stomp/client");
        registry.addViewController("test").setViewName("websocket/test");
    }
}
