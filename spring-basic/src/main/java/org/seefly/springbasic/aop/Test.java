package org.seefly.springbasic.aop;

import org.junit.Before;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 *
 * AOP初始化流程
 * 1、传入主配置类，创建IOC容器
 * 2、创建完成后调用{@link AbstractApplicationContext#refresh()}刷新容器
 * 3、调用 {@link AbstractApplicationContext#registerBeanPostProcessors} 就是创建bean的后置处理器，来拦截bean的创建。
 * 4、先在bean工厂中拿出所有BeanPostProcessor的实现类，这些其中实现了PriorityOrdered的先注册，其次是实现了Ordered的，再其次就是啥都没实现的。
 *     ps:这里面就有@EnableAspectJAutoProxy里面注册的哪个internalAutoProxyCreator的定义信息
 *    【internalAutoProxyCreator的创建】
 *    1）、在调用beanFactory.getBean(ppName, BeanPostProcessor.class)从工厂中拿这个类实例的时候发现没有，然后就要创建
 *    2）、最终调用到{@link AbstractAutowireCapableBeanFactory#doCreateBean}
 *          1、{@link AbstractAutowireCapableBeanFactory#createBeanInstance} 创建bean
 *          2、{@link AbstractAutowireCapableBeanFactory#populateBean} 填充属性
 *          3、{@link AbstractAutowireCapableBeanFactory#initializeBean} 初始化bean
 *              1)、{@link AbstractAutowireCapableBeanFactory#invokeAwareMethods}引用所有的Aware接口的回调方法
 *              2)、{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization}调用一下所有的bean的后置处理器
 *              3)、{@link AbstractAutowireCapableBeanFactory#invokeInitMethods} 调用一下bean的初始化方法，例如InitializingBean接口
 *              4)、{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization}调用后置处理器的after
 *  5、将后置处理器放到 beanFactory.addBeanPostProcessor(postProcessor);
 * ====================上面是初始化BeanPostProcess===================
 *  6、初始化剩余的单实例对象{@link AbstractApplicationContext#finishBeanFactoryInitialization}
 *      1）、{@link DefaultListableBeanFactory#preInstantiateSingletons()}
 *      2）、getBean()
 *              -> {@link AbstractBeanFactory#doGetBean(java.lang.String, java.lang.Class, java.lang.Object[], boolean)} ->
 *                  -> getSingleton() 创建之前先看缓存、父类Bean工厂中有没有，有的话就不用创建直接使用。
 *                  -> 没有则调用{@link AbstractAutowireCapableBeanFactory#createBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[])}
 *                      -> {@link AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition)}
 *                          这个方法注释说给后置处理器一个机会，如果后置处理器返回了一个代理Bean，则就用后置处理器代理的那个，否则就创建。并直接返回
 *                          -> {@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInstantiation(java.lang.Class, java.lang.String)}
 *                              这块会调用所有的注册的{@link InstantiationAwareBeanPostProcessor}的实例的postProcessBeforeInstantiation方法，AOP代理注册的那个也属于他的实现类，所以在这里会被应用。
 *                      -> {@link AbstractAutowireCapableBeanFactory#doCreateBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[])}
 *                          如果上面的那个方法没有返回一个代理Bean则就直接创建，创建的逻辑又回到了4.2了。创建完成之后就返回
 *
 *
 *
 *
 *
 *
 * @author liujianxin
 * @date 2019-05-04 20:16
 */
public class Test{
    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void before(){
        applicationContext = new AnnotationConfigApplicationContext(AopConfig.class);
        System.out.println("容器初始化完毕...");
    }

    @org.junit.Test
    public void test(){
        People bean = applicationContext.getBean(People.class);
        bean.sing("sdf");
    }

}
