package org.seefly.springannotation.factorybean;

import org.seefly.springannotation.entity.Person;
import org.springframework.beans.factory.FactoryBean;

/**
 * 创建一个指定实例的工厂类，实现Spring提供的接口
 * 通过getObject方法获取该实例，getObjectType指定实例类型，isSingleton指定是否单例。
 * 向容器中添加该FactoryBean的实际作用不是添加这个FactoryBean本身，而是它产生的Bean。
 *
 * 在SpringBoot在和其他框架整合的时候使用的比较多。
 * @author liujianxin
 * @date 2018-12-23 20:57
 */
public class PersonFactoryBean implements FactoryBean<Person> {
    @Override
    public Person getObject() throws Exception {
        return new Person("shuaige",16);
    }

    @Override
    public Class<?> getObjectType() {
        return Person.class;
    }

    /**
     * 是否单例
     * @return true 单例， false 多实例
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
