package org.seefly.springannotation.process.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 提供两个钩子函数
 * 1、【初始化前】的钩子方法
 * 2、【初始化后】的钩子方法
 *
 *
 * 这个放入容器会在任何组件初始化之前以及之后执行方法
 * 下面说一些它的主要子类，以及作用
 * 1、ApplicationContextAwareProcessor
 *   看一下它的注释就知道了，如果你实现了EnvironmentAware接口，他就给你注入一个Environment
 *   实现了ApplicationContextAware，给你注入一个上下文，等等
 * 2、BeanValidationPostProcessor
 *     数据校验的，像是web环境，接口里面接一个对象，校验就靠这个
 * 4、CommonAnnotationBeanPostProcessor
 *      PostConstruct PreDestroy的生效就靠这个，还有@Resource注解也靠这个。
 * 5、 AutowiredAnnotationBeanPostProcessor
 *   {@link Autowired}就是这个
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
