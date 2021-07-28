package org.seefly.aop.basic.evolution;

import org.seefly.aop.basic.model.User;
import org.seefly.aop.basic.evolution.service.UserService;
import org.seefly.aop.basic.evolution.service.impl.UserServiceImpl;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * 之前的自动创建代理的已经解决了一个一个创建代理的问题
 * 工作方式就是从容器中获取bean根据自己的规则决定需不需要在这个bean上应用advice advisor等
 *
 * 现在使用{@link DefaultAdvisorAutoProxyCreator} 就高级了，看名字就是增强器自动代理创建
 * 他会从容器中获取所有的Advisor，在容器中所有的Bean上应用增强器的匹配规则，匹配的上就创建代理。
 *
 * @author liujianxin
 * @date 2019/8/21 17:29
 */
public class Step4 {
    
    
    //@Configuration
    //@Import(DefaultAdvisorAutoProxyCreator.class)
    public static class Config {
        
        public static void main(String[] args) {
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                    Config.class);
            // 直接获取被代理对象
            UserService userService = applicationContext.getBean(UserService.class);
            // 正则 org.seefly.springaop.service.*.UserServiceImpl.create.* 可以匹配到该方法
            User a = userService.createUser("a", 1);
            
            // 匹配不到这个
            User user = userService.queryUser("");
        }
        
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
        
        @Bean
        public MethodBeforeAdvice beforeAdvice() {
            return (method, args, target) -> System.out
                    .println("准备执行方法" + method.getName() + " 参数列表" + Arrays.toString(args));
        }
        
        
        @Bean
        public RegexpMethodPointcutAdvisor regexpAdvisor() {
            RegexpMethodPointcutAdvisor advisor = new RegexpMethodPointcutAdvisor();
            advisor.setAdvice(beforeAdvice());
            // 支持正则匹配
            // 匹配create开头的方法
            advisor.setPatterns("org.seefly.springaop.service.*.UserServiceImpl.create.*");
            return advisor;
        }
        
        
    }
}
