package org.seefly.springaop.demo;

import org.seefly.springaop.advice.LogAfterAdvice;
import org.seefly.springaop.advice.LogBeforeAdvice;
import org.seefly.springaop.domain.User;
import org.seefly.springaop.service.UserService;
import org.seefly.springaop.service.impl.UserServiceImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 演示Spring 1.2中怎么使用AOP
 * 整个流程很简单，需要AOP要有以下几个元素
 * 1、通知
 * 2、被代理对象
 * 3、创建代理
 * 这样我们创建代理，将被代理对象设置进去，再指定通知。
 * 我们在使用的时候使用这个代理即可
 *
 * 组件介绍
 * {@link LogAfterAdvice} 通知，我们需要织入的逻辑
 * {@link UserService} 需要被增强的对象
 * {@link ProxyFactoryBean} 代理对象，给他设置通知，被代理对象，接口。他会应用通知对被代理对象进行增强
 *  形成一个代理对象放到容器中，使用的时候使用这个代理对象即可
 *
 *
 *
 * 缺陷
 * 1、可以看到，UserService需要代理，我们就要手动创建一个代理对象，放到容器里。
 *  用的时候从容器中取出来。如果多个对象都需要代理，我们需要手动的一个一个的去指定。很麻烦
 * 2、通知的粒度只细化到类上，类中所有的方法均被拦截了
 * 3、获取的时候需要获取代理对象，不能直接使用被代理对象
 *
 * 为了解决这种问题，请往下看
 *
 *
 * https://javadoop.com/post/spring-aop-intro
 * @author liujianxin
 * @date 2019/8/20 17:32
 */
public class Step1 {
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        UserService userService = (UserService) applicationContext.getBean("userServiceProxy");
        User a = userService.createUser("a", 1);
        System.out.println(a);
    }
    
    
    //@Configuration
    public static class Config {
        
        /**
         * 给定一个后置通知
         */
        @Bean
        public LogAfterAdvice logAfterAdvice() {
            return new LogAfterAdvice();
        }
        
        /**
         * 给定一个前置通知
         */
        @Bean
        public LogBeforeAdvice logBeforeAdvice() {
            return new LogBeforeAdvice();
        }
        
        /**
         * 给定一个需要被代理的对象
         */
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
        
        /**
         * 需要对被代理对象创建一个代理 在使用的时候从容器中取出这个代理即可实现代理功能
         */
        @Bean
        public ProxyFactoryBean userServiceProxy() throws ClassNotFoundException {
            // 创建代理
            ProxyFactoryBean factoryBean = new ProxyFactoryBean();
            // 指定代理的接口 可以是多个
            Class[] arr = {UserService.class};
            factoryBean.setProxyInterfaces(arr);
            // 传入被代理对象
            factoryBean.setTarget(userService());
            // 指定通知
            factoryBean.setInterceptorNames("logBeforeAdvice", "logAfterAdvice");
            return factoryBean;
        }
        
    }
    
}
