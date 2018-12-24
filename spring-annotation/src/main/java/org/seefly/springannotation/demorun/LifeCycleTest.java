package org.seefly.springannotation.demorun;

import org.junit.Before;
import org.junit.Test;
import org.seefly.springannotation.config.LifeCycleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author liujianxin
 * @date 2018-12-23 21:43
 */
public class LifeCycleTest {
    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void before(){
        applicationContext = new AnnotationConfigApplicationContext(LifeCycleConfig .class);
        System.out.println("容器初始化完毕...");
    }

    /**
     * 实现接口的方式指定初始化、销毁方法
     */
    @Test
    public void testFlower(){
        Object flower = applicationContext.getBean("org.seefly.springannotation.entity.Flower");
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
}
