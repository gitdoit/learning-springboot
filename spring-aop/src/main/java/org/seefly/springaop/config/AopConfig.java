package org.seefly.springaop.config;

import org.seefly.springaop.aspect.MyAspect;
import org.seefly.springaop.component.People;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author liujianxin
 * @date 2019-04-22 21:12 EnableAspectJAutoProxy注解里面用@Import(AspectJAutoProxyRegistrar.class)引入了一个Registrar
 * 这个AspectJAutoProxyRegistrar实现了ImportBeanDefinitionRegistrar接口，这个接口之前学习过，就是自定义引入bean 最后引入的是
 * internalAutoProxyCreator=AnnotationAwareAspectJAutoProxyCreator ->{@link AspectJAwareAdvisorAutoProxyCreator}
 * ->{@link AbstractAdvisorAutoProxyCreator} ->{@link AbstractAutoProxyCreator} implements {@link
 * SmartInstantiationAwareBeanPostProcessor}, {@link BeanFactoryAware} ->其中SmartInstantiationAwareBeanPostProcessor
 * extends {@link InstantiationAwareBeanPostProcessor}这个接口 用来在Bean实例化前进行拦截处理的
 * <p>
 * 综上可以看出: 这个注解的作用是引入了一个Registrar，这个Registrar具有BeanPostProcessor和BeanFactoryAware的功能 所以可以对bean进行后置处理，以及可以获取BeanFactory
 * <p>
 * 逻辑： 从最下面开始 AbstractAutoProxyCreator.setBeanFactory() AbstractAutoProxyCreator.后置处理器逻辑
 * <p>
 * AbstractAdvisorAutoProxyCreator.setBeanFactory() -> initBeanFactory()
 * <p>
 * AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    
    @Bean
    public MyAspect myAspect() {
        return new MyAspect();
    }
    
    @Bean
    public People people() {
        return new People();
    }
}
