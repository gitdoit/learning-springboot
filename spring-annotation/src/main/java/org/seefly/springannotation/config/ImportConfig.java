package org.seefly.springannotation.config;

import org.seefly.springannotation.condition.OperatingSystemCondition;
import org.seefly.springannotation.entity.*;
import org.seefly.springannotation.selector.MyImportBeanDefinitionRegistrar;
import org.seefly.springannotation.selector.MyImportSelector;
import org.springframework.context.annotation.*;

/**
 * {@link Import}用于根据传入的参数向容器中添加指定组件
 *  1、参数直接指定类型
 *  2、参数指定一个{@link ImportSelector}的实例，则会根据接口返回值向容器添加组件
 *  3、参数指定一个{@link ImportBeanDefinitionRegistrar}的实例，自定义手动注册组件
 */
@Configuration
@Import({ImportBean.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class ImportConfig {

    @Bean
    public Person person(){
        return new Person("liu",12);
    }


    /**
     * 默认为单实例的。
     * prototype 多实例的，在多实例情况下，为懒加载，即在获取这里实例的时候才会创建一个。
     * singleton 单实例的，在单实例情况下，在容器初始化的时候会创建单实例放入容器。
     *  {@link Lazy},对于单实例的情况下，容器在初始化的时候不会创建单实例，只有在第一次调用的时候次啊会创建
     * request mvc环境下 一个请求一个实例
     * session mvc环境下，一次会话 一个实例
     */
    @Lazy
    @Scope("singleton")
    @Bean
    public ScopeBean scopeBean(){
        System.out.println("创建scopeBean实例...........");
        return new ScopeBean();
    }


    /**
     * 根据自定义条件向容器中添加实例
     */
    @Conditional({OperatingSystemCondition.class})
    @Bean
    public ConditionBean conditionBean(){
        System.out.println("创建windows实例....");
        return new ConditionBean("windows 10");
    }

    /**
     * 向容其中添加该FactoryBean产生的实例(Person)
     * 该FactoryBean实际的作用就是生产Person实例。
     */
    @Bean
    public PersonFactoryBean personFactoryBean(){
        return new PersonFactoryBean();
    }


}
