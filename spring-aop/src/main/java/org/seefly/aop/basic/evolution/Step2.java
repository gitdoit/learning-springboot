package org.seefly.aop.basic.evolution;

import org.aopalliance.intercept.MethodInterceptor;
import org.seefly.aop.basic.model.User;
import org.seefly.aop.basic.evolution.service.UserService;
import org.seefly.aop.basic.evolution.service.impl.UserServiceImpl;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 由上一步骤得知
 * 我们没法对类中的指定方法做拦截，只能拦截该类下的所有方法。
 *
 * 现在我们引入增强器  {@link NameMatchMethodPointcutAdvisor} {@link PointcutAdvisor} extends {@link Advisor}
 * 我们知道（其实不知道 ）增强器是Spring里的概念，AspectJ中没有对应的概念。
 *  一个增强器包含两个基本元素：匹配规则(就是切入点)、通知
 *  增强器负责匹配，匹配到的切入点交由Advice执行逻辑。这个匹配规则大部分都是一个Pointcut
 *  而 {@link NameMatchMethodPointcutAdvisor}增强器则是可以根据传入的方法名称进行匹配
 *  还有其他很多类型的增强器
 *
 * 组件介绍
 * {@link MethodInterceptor}  另一种形式的通知，这里相当于拦截器。拦截方法执行前后
 * {@link NameMatchMethodPointcutAdvisor} 方法名匹配增强器，根据方法名进行匹配，并执行其内部组合的通知的逻辑
 *
 *
 * 但是还有一个缺陷就是需要为每个需要被增强的对象做一个单独的代理，这里没有解决
 * 只解决了拦截粒度的细化
 *
 *
 * @author liujianxin
 * @date 2019/8/21 9:45
 */
public class Step2 {
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        UserService userService = (UserService) applicationContext.getBean("userServiceProxy");
        // 只针对该方法做拦截，且为前置通知
        User a = userService.createUser("a", 1);
        
        // 不拦截该方法
        //User a = userService.queryUser("sdf");
        System.out.println(a);
    }
    
    
    //@Configuration
    public static class Config {
        
        @Bean
        public AfterReturningAdvice afterAdvice() {
            return (returnValue, method, args, target) -> System.out
                    .println("方法" + method.getName() + "执行完毕，返回值" + returnValue);
        }
        
        @Bean
        public MethodBeforeAdvice beforeAdvice() {
            return (method, args, target) -> System.out
                    .println("准备执行方法" + method.getName() + " 参数列表" + Arrays.toString(args));
        }
        
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
        
        @Bean
        public MethodInterceptor methodInterceptor() {
            return invocation -> {
                System.out.println("方法拦截");
                return invocation.proceed();
            };
        }
        
        
        /**
         * 一个增强器包含一个通知和一个切入点 所以它有 “匹配规则” 和 “执行逻辑”两个功能
         */
        @Bean
        public NameMatchMethodPointcutAdvisor logAdvisor() {
            NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
            // 一个增强器只能包含一个通知
            advisor.setAdvice(afterAdvice());
            // 但可以匹配多个方法
            // The default implementation checks for "xxx*", "*xxx" and "*xxx*" matches,as well as direct equality. Can be overridden in subclasses.
            advisor.setMappedNames("createUser");
            return advisor;
        }
        
        @Bean
        public ProxyFactoryBean userServiceProxy() throws ClassNotFoundException {
            // 创建代理
            ProxyFactoryBean factoryBean = new ProxyFactoryBean();
            // 指定代理的接口 可以是多个
            Class[] arr = {UserService.class};
            factoryBean.setProxyInterfaces(arr);
            // 传入被代理对象
            factoryBean.setTarget(userService());
            // 【使用增强器】 logAdvisor
            // 这里还可以传入 methodInterceptor
            factoryBean.setInterceptorNames("logAdvisor", "methodInterceptor");
            return factoryBean;
        }
        
        
    }
}
