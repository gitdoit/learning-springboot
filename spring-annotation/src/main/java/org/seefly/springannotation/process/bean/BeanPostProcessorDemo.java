package org.seefly.springannotation.process.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.BeanValidationPostProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.nio.file.Paths;

/**
 * 提供两个钩子函数
 * 1、【初始化前】的钩子方法
 * 2、【初始化后】的钩子方法
 *
 *
 * 这个放入容器会在任何组件初始化之前以及之后执行方法
 * 【重要实现子类】
 * 下面说一些它的主要子类，以及作用
 * 1、ApplicationContextAwareProcessor
 *     对各种Aware接口做支持，在初始化前注入各种东西
 * 2、{@link BeanValidationPostProcessor}
 *     数据校验的，像是web环境，接口里面接一个对象，校验就靠这个
 * 3、{@link CommonAnnotationBeanPostProcessor}
 *      实现了对 {@link PostConstruct}、{@link PreDestroy}注解的支持（实际上是它的父类 InitDestroyAnnotationBeanPostProcessor 干的活）
 *      实现了对 {@link Resource}注解的支持(实际上是父类 {@link MergedBeanDefinitionPostProcessor}干的活)
 * 4、 {@link AutowiredAnnotationBeanPostProcessor}
 *      实现了对 {@link Autowired} 、{@link Value}、@Inject 注解的支持
 * @author liujianxin
 * @date 2018-12-26 21:26
 */
@Component
public class BeanPostProcessorDemo implements BeanPostProcessor {

    /**
     * 在任何【初始化】动作之前执行,在调用自定的初始化方法前执行
     *
     * 如果返回null，则后续的调用初始化钩子方法不再执行
     * @param bean 这个bean
     * @param beanName 这个bean在容器中的名字
     */

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // System.out.println("BeanPostProcessor--"+beanName+bean.getClass());

        return bean;
    }

    /**
     * 在调用初始化方法之后的钩子方法
     * @param bean 这个bean
     * @param beanName 这个bean在容器中的名字
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // System.out.println("postProcessAfterInitialization--"+beanName+bean.getClass());
        return bean;
    }
}
