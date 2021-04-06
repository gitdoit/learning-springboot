package org.seefly.springannotation;

import org.junit.Before;
import org.junit.Test;
import org.seefly.springannotation.imports.ImportConfig;
import org.seefly.springannotation.entity.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 演示使用{@link AnnotationConfigApplicationContext}来加载配Java配置文件
 * @author liujianxin
 * @date 2018-12-18 23:58
 */
public class AnnoConfigTest extends BaseTest {
    private ApplicationContext applicationContext;

    @Before
    public void before(){
        applicationContext = new AnnotationConfigApplicationContext(ImportConfig.class);
        System.out.println("容器初始化完毕...");
    }

    /**
     * 基本方法
     */
    @Test
    public void test(){
        Person bean = applicationContext.getBean(Person.class);
        System.out.println(bean);
    }

    /**
     * 演示实例的作用域
     */
    @Test
    public void testScope(){
        Object scopeBean = applicationContext.getBean("scopeBean");
        Object scopeBean1 = applicationContext.getBean("scopeBean");
        System.out.println(scopeBean == scopeBean1);
    }

    /**
     * 查看容器中的所有注册的组件
     */
    @Test
    public void showBeans(){
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name : beanDefinitionNames){
            System.out.println(name);
        }
    }

    /**
     * 查看PersonFactoryBean是否生效
     */
    @Test
    public void testFactoryBean(){
        Object person = applicationContext.getBean("personFactoryBean");
        // 如果要获取FactoryBean本身，则在ID前添加一个 & 即可
        Object factoryBean = applicationContext.getBean("&personFactoryBean");
        // class org.seefly.springannotation.entity.Person
        System.out.println(person.getClass());
        // class org.seefly.springannotation.factorybean.PersonFactoryBean
        System.out.println(factoryBean.getClass());
    }
}
