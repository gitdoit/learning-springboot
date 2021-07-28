package org.seefly.aop.basic.evolution;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.seefly.aop.basic.model.People;
import org.springframework.context.annotation.*;

/**
 * @author liujianxin
 * @date 2019/8/22 10:41
 */
public class Step5 {
    
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        People bean = applicationContext.getBean(People.class);
        bean.sing("哈哈哈");
    }
    
    
    //@Import(MyAspect.class)
    //@Configuration
    /**
     * exposeProxy = true是否暴露代理对象，这个用于解决在增强方法中
     * 调用该Bean的另一个被增强的方法不走AOP的问题 -> ((People)AopContext.currentProxy()).sing("");
     * proxyTargetClass = false表示不强制使用代理方式，也就是说有接口就走JDK，没有就走CGLIB
     */
    //@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = false)
    public static class Config {
        
        @Bean
        public People people() {
            return new People();
        }
    }
    
    
    /**
     * 关于切面优先级 可以通过@Order(1)注解来标注当前切面中的通知方法的优先级 值越小优先级越高，另外优先级相同则依赖于AOP解析顺序。 还有事务也是以来AOP的，它的优先级是最低的
     */
    //@Order(1)
   // @Aspect
    public static class MyAspect {
        
        @Pointcut("execution(* org.seefly.aop.basic.model.People.*(..))")
        public void pointCut() {
        }
        
        @Before("pointCut()")
        public void beforeSing(JoinPoint joinPoint) {
            System.out.println("Before...");
        }
        
        @After("pointCut()")
        public void after() {
            System.out.println("After...");
        }
        
        @AfterReturning("pointCut()")
        public void afterReturning() {
            System.out.println("AfterReturning...");
        }
        
        @AfterThrowing("pointCut()")
        public void afterThrowing() {
            System.out.println("AfterThrowing");
        }
    }
    
    
}
