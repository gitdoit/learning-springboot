package org.seefly.springannotation.demorun;

import org.junit.Test;
import org.seefly.springannotation.config.MainConfig;
import org.seefly.springannotation.entity.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 演示使用{@link AnnotationConfigApplicationContext}来加载配Java配置文件
 * @author liujianxin
 * @date 2018-12-18 23:58
 */
public class AnnotationConfigApplicationTest {

    @Test
    public void test(){
        //加载配置类
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        Person bean = applicationContext.getBean(Person.class);
        System.out.println(bean);
    }
}
