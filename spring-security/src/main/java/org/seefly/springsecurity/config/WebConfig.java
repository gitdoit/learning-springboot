package org.seefly.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liujianxin
 * @date 2018-12-13 23:12
 */
//@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ipLogin").setViewName("/ipLogin");
        registry.addViewController("/401").setViewName("/401");
        registry.addViewController("/403").setViewName("/403");
        registry.addViewController("/oauth/403").setViewName("/oauth/403");
        registry.addViewController("/oauth/401").setViewName("/oauth/401");
        registry.addViewController("/badmima").setViewName("/badmima");
        //registry.addViewController("/login").setViewName("login");
    }
}
