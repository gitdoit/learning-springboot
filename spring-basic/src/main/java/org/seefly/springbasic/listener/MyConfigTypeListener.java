package org.seefly.springbasic.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Map;

/**
 * 通过约定配置的方式，在META-INF/spring.factories下配置的监听器
 * org.springframework.context.ApplicationListener=org.seefly.springbasic.listener.MyConfigTypeListener
 *
 * @author liujianxin
 * @date 2021/4/8 10:35
 */
public class MyConfigTypeListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        // System.out.println("约定配置方式获取到事件：ApplicationStartingEvent");
        ConfigurableEnvironment environment = event.getEnvironment();
        MutablePropertySources jvm = event.getEnvironment().getPropertySources();
        Map<String, Object> system = environment.getSystemProperties();
        System.out.println(jvm);
        System.out.println(system);
    }
}
