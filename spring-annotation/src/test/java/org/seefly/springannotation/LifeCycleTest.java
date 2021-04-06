package org.seefly.springannotation;

import org.junit.Before;
import org.junit.Test;
import org.seefly.springannotation.lifecycle.LifeCycleConfig;
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
 * @author liujianxin
 * @date 2018-12-23 21:43
 */
public class LifeCycleTest extends BaseTest {
    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void before(){
        applicationContext = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        System.out.println("容器初始化完毕...");
    }

    /**
     * 实现接口的方式指定初始化、销毁方法
     */
    @Test
    public void testFlower(){
        Object flower = applicationContext.getBean("org.seefly.springannotation.lifecycle.LifeByInterface");
        System.out.println(flower.getClass());
        applicationContext.close();
    }

    /**
     * 通过@Bean注解指定初始化、销毁方法
     */
    @Test
    public void test(){
        Object tree = applicationContext.getBean("tree");
        System.out.println(tree.getClass());
        applicationContext.close();
    }

    @Test
    public void testJSR(){
        Object tree = applicationContext.getBean("org.seefly.springannotation.lifecycle.LifeByJSR");
        System.out.println(tree.getClass());
        applicationContext.close();
    }

    @Test
    public void testPost(){
        Object tree = applicationContext.getBean("org.seefly.springannotation.process.bean.LifeBeanPost");
        System.out.println(tree.getClass());
        applicationContext.close();
    }
}
