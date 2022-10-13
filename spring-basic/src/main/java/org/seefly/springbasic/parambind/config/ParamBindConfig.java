package org.seefly.springbasic.parambind.config;

import org.seefly.springbasic.parambind.beans.RiskTableConfig;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * @author liujianxin
 * @date 2018-08-03 16:33
 * 描述信息：该类用来演示springboot2.0中新的API-Binder
 * 该API作用和之前的@ConfigurationProperties(prefix = "peopro")作用类似
 * 但用法上有些区别
 **/
@Configuration
public class ParamBindConfig {
//
//    @Bean
//    public BinderBean binderBean(ApplicationContext applicationContext){
//        Binder binder = Binder.get(applicationContext.getEnvironment());
//        BinderBean binderTest = binder.bind("binder", Bindable.of(BinderBean.class)).get();
//        return binderTest;
//    }
//
//
//    @Bean
//    public ConfigRef configRef(ApplicationContext applicationContext){
//        Binder binder = Binder.get(applicationContext.getEnvironment());
//        ConfigRef binderTest = binder.bind("bar", Bindable.of(ConfigRef.class)).get();
//        return binderTest;
//    }
//
//    @Bean
//    public Map<String, MapConfig> mapConfig(ApplicationContext applicationContext){
//        Binder binder = Binder.get(applicationContext.getEnvironment());
//        Map<String, MapConfig> bar = binder.bind("map", Bindable.mapOf(String.class, MapConfig.class)).get();
//        return bar;
//    }

    @Bean
    public RiskTableConfig mapConfig(ApplicationContext applicationContext) {
        Binder binder = Binder.get(applicationContext.getEnvironment());

        RiskTableConfig config= binder.bind("risk-table", Bindable.of(RiskTableConfig.class)).get();

        System.out.println(config);
        return config;
    }


}
