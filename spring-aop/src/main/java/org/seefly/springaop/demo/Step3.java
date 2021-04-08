package org.seefly.springaop.demo;

import org.seefly.springaop.domain.User;
import org.seefly.springaop.service.UserService;
import org.seefly.springaop.service.impl.UserServiceImpl;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
/**
 * 步骤2的需要对被代理对象一个一个的单独配置的缺陷还没解决
 * 这里可以使用{@link BeanNameAutoProxyCreator}
 *  查询容器中所有的Bean根据配置的Bean的匹配规则来匹配，如果匹配的上
 *  就应用Advice Advisor Interceptor..来创建代理对象替代原有的对象放入容器
 *  使用的时候获取被代理对象就行，不用像之前那样获取代理对象。
 *
 * 组件介绍
 * {@link RegexpMethodPointcutAdvisor} 另一种增强器，之前的按方法名匹配的增强器不够灵活，这个可以支持正则
 * {@link BeanNameAutoProxyCreator} ...见上
 *
 *
 * @author liujianxin
 * @date 2019/8/21 11:39
 */
public class Step3 {
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        // 直接获取被代理对象
        UserService userService = applicationContext.getBean(UserService.class);
        // 只针对该方法做拦截，且为前置通知
        User a = userService.createUser("a", 1);
    }
    
    
    @Configuration
    public static class Config {
        
        
        @Bean
        public MethodBeforeAdvice beforeAdvice() {
            return (method, args, target) -> System.out
                    .println("准备执行方法" + method.getName() + " 参数列表" + Arrays.toString(args));
        }
        
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
        
        /**
         * 这个增强器比之前的{@link NameMatchMethodPointcutAdvisor} 增强器要高级一点，之前的只能指定方法名称，而这个支持正则匹配方法名
         */
        @Bean
        public RegexpMethodPointcutAdvisor regexpAdvisor() {
            RegexpMethodPointcutAdvisor advisor = new RegexpMethodPointcutAdvisor();
            advisor.setAdvice(beforeAdvice());
            // 支持正则匹配
            advisor.setPatterns("org.seefly.springaop.service.impl.UserServiceImpl.*");
            return advisor;
        }
        
        /**
         * 自动代理创建器 该类能够根据BeanNames属性自动在容器中获取指定的Bean创建代理
         */
        @Bean
        public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
            BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
            // 跟之前一样，可传Advice Advisor MethodInterceptor
            // 我们使用正则增强器
            proxyCreator.setInterceptorNames("regexpAdvisor");
            // 不再传入指定的被代理对象，而是传入Bean的名字，支持模糊匹配
            proxyCreator.setBeanNames("*Service");
            return proxyCreator;
        }
    }
}
