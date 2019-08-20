package org.seefly.springaop.demo;

import org.seefly.springaop.service.UserService;
import org.seefly.springaop.service.impl.UserServiceImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujianxin
 * @date 2019/8/20 17:32
 */
public class Step1 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config .class);
    }



    @Configuration
    public static class Config{

        @Bean
        public UserService userService(){
            return new UserServiceImpl();
        }

        @Bean
        public ProxyFactoryBean userServiceProxy() throws ClassNotFoundException {
            ProxyFactoryBean factoryBean = new ProxyFactoryBean();
            Class[] arr = {UserService.class};
            factoryBean.setProxyInterfaces(arr);
            factoryBean.setTarget(UserServiceImpl.class);
            return factoryBean;
        }

    }

}
