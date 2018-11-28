package org.seefly.springweb02.config;

import org.seefly.springweb02.beans.BinderBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujianxin
 * @date 2018-08-03 16:33
 * 描述信息：该类用来演示springboot2.0中新的API-Binder
 * 该API作用和之前的@ConfigurationProperties(prefix = "peopro")作用类似
 * 但用法上有些区别
 **/
@Configuration
public class ParamBindConfig {

    @Bean
    public BinderBean binderBean(ApplicationContext applicationContext){
        Binder binder = Binder.get(applicationContext.getEnvironment());
        BinderBean binderTest = binder.bind("binder", Bindable.of(BinderBean.class)).get();
        System.out.println(binderTest);
        return binderTest;
    }

}
