package org.seefly.springannotation.lifecycle;

import org.seefly.springannotation.process.bean.BeanPostProcessorDemo;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * spring组件的生命周期学习
 * 关于bean的初始化以及销毁方法可以由下面几个方式来指定
 * 1、在@Bean中指定该bean的初始化、销毁方法
 * 2、实现接口{@link InitializingBean}提供初始化方法
 *    实现接口{@link DisposableBean} 提供销毁方法
 * 3、在bean的方法上标注{@link PostConstruct}注解，表明它为一个初始化方法，我爱用。
 *    在bean的方法上标注{@link PreDestroy} 注解，表明他为一个销毁方法。
 * 4、使用强大的{@link BeanPostProcessor}接口，上面的那些初始化以及销毁方法为什么生效，就是因为这个接口。
 *    当然，这些都是交给不同的子类去实现的。我们也可以实现这个接口来自定义骚操作。需要注意的是，它并不是针对某一个
 *    组件生效，它是在容器中每个的组件构造之后都会执行一遍你定义的逻辑。
 *    它的主要子类的都干的啥->{@link BeanPostProcessorDemo}
 *
 * 5、使用Lifecycle接口,他会在容器刷新\停止的时候会去进行回调,另外eureka启动就是靠这个接口来实现的
 *
 * @author liujianxin
 * @date 2018-12-23 21:43
 */
public class LifecycleApplicationDemoRun {
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
    }
    
}
