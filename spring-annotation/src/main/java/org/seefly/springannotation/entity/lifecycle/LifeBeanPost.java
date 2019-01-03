package org.seefly.springannotation.entity.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 这个放入容器会在任何组件初始化之前以及之后执行方法
 * 下面说一些它的主要子类，以及作用
 * 1、ApplicationContextAwareProcessor
 *   看一下它的注释就知道了，如果你实现了EnvironmentAware接口，他就给你注入一个Environment
 *   实现了ApplicationContextAware，给你注入一个上下文，等等
 * 2、BeanValidationPostProcessor
 *     数据校验的，像是web环境，接口里面接一个对象，校验就靠这个
 * 4、InitDestroyAnnotationBeanPostProcessor
 *  PostConstruct PreDestroy的生效就靠这个，还有@Resource注解也靠这个。
 * 5、 AutowiredAnnotationBeanPostProcessor
 *   {@link Autowired}就是这个
 * @author liujianxin
 * @date 2018-12-26 21:26
 */
public class LifeBeanPost implements BeanPostProcessor {

    /**
     * 在任何初始化动作之前执行
     * @param bean 这个bean
     * @param beanName 这个bean在容器中的名字
     */

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor--"+beanName+bean.getClass());

        return bean;
    }

    /**
     *
     * @param bean 这个bean
     * @param beanName 这个bean在容器中的名字
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization--"+beanName+bean.getClass());
        return bean;
    }
}
