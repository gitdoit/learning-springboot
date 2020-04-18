package org.seefly.webflux.router;

import org.seefly.webflux.controller.HelloWorldHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 路由规则定义
 * @author liujianxin
 * @date 2020/4/18 21:09
 */
@Configuration
public class RouterConfig {

    /**
     * 很好理解
     * RouterFunctions.route的第一个参数是规则断言
     * 第二个则是匹配后的处理逻辑
     */
    @Bean
    public RouterFunction<ServerResponse> rout(HelloWorldHandler helloWorldHandler){
        return RouterFunctions.route(RequestPredicates.GET("/hello")
        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),helloWorldHandler::hello);
    }
}
