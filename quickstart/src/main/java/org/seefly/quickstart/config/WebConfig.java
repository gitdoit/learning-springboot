package org.seefly.quickstart.config;

import lombok.extern.slf4j.Slf4j;
import org.seefly.quickstart.config.resolve.AnnoArgumentResolver;
import org.seefly.quickstart.config.resolve.CvsArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-09-11 19:49
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

   @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AnnoArgumentResolver());
        resolvers.add(new CvsArgumentResolver());
    }


}
