package org.seefly.springbasic.config;

import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author liujianxin
 * @date 2019-04-22 21:12
 * EnableAspectJAutoProxy注解里面用@Import(AspectJAutoProxyRegistrar.class)引入了一个Registrar
 * 这个AspectJAutoProxyRegistrar实现了ImportBeanDefinitionRegistrar接口，这个接口之前学习过，就是自定义引入bean
 * 最后引入的是
 *  {@link AnnotationAwareAspectJAutoProxyCreator}
 *      ->{@link AspectJAwareAdvisorAutoProxyCreator}
 *          ->{@link AbstractAdvisorAutoProxyCreator}
 *              ->{@link AbstractAutoProxyCreator} implements {@link SmartInstantiationAwareBeanPostProcessor}, {@link BeanFactoryAware}
 *  综上可以看出:
 *      这个注解的作用是引入了一个Registrar，这个Registrar具有BeanPostProcessor和BeanFactoryAware的功能
 *      所以可以对bean进行后置处理，以及可以获取BeanFactory
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
}
