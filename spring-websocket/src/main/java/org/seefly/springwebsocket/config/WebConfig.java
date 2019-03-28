package org.seefly.springwebsocket.config;


import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
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
        // tomcat 实现原生Websocket + 前端原生Websocket = 音频上传
        registry.addViewController("tomcat").setViewName("websocket/tomcat");
        // spring 实现原生WebSocket + 前端原生WebSocket = 音频上传、音频接收及播放
        registry.addViewController("spring").setViewName("websocket/spring");
        // spring 实现Stomp + 前端Stomp_client = 音频上传、音频接收及播放  开发中
        registry.addViewController("stomp-client").setViewName("stomp/client");
        // spring 实现Stomp + 前端Stomp = 开发中
        registry.addViewController("stomp").setViewName("stomp/stomp");
        // SIP 服务
        registry.addViewController("sip").setViewName("stomp/sip");
        // tomcat 实现原生WebSocket + 前端sockJs = 发送文字 未完成
        registry.addViewController("client").setViewName("client");

    }


    /**
     *   ssl:
     *     key-store: classpath:server.keystore
     *     key-password: 12345678
     *     key-store-type: JKS
     *     key-alias: server_cert
     * @return
     */
    //@Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                //confidential
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    //@Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }


}
