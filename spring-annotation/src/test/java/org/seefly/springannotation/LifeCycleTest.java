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
