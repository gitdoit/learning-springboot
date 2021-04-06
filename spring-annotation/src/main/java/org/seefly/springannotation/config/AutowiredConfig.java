package org.seefly.springannotation.config;

import org.seefly.springannotation.scope.ScopeBean;
import org.seefly.springannotation.factorybean.PersonFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 *
 *
 *
 * @author liujianxin
 * @date 2019/9/15 16:03
 */
@Configuration
public class AutowiredConfig {
    
    /**
     * 默认为单实例的。
     * prototype 多实例的，在多实例情况下，为懒加载，即在获取这里实例的时候才会创建一个。
     * singleton 单实例的，在单实例情况下，在容器初始化的时候会创建单实例放入容器。
     *  {@link Lazy},对于单实例的情况下，容器在初始化的时候不会创建单实例，只有在第一次调用的时候次啊会创建
     * request mvc环境下 一个请求一个实例
     * session mvc环境下，一次会话 一个实例
     */
    @Lazy
    @Scope("singleton")
    @Bean
    public ScopeBean scopeBean(){
        System.out.println("创建scopeBean实例...........");
        return new ScopeBean();
    }
    
    
    /**
     * 向容其中添加该FactoryBean产生的实例(Person)
     * 该FactoryBean实际的作用就是生产Person实例。
     */
    @Bean
    public PersonFactoryBean personFactoryBean(){
        return new PersonFactoryBean();
    }
}
