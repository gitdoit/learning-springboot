package org.seefly.springannotation.process.bean;

import org.seefly.springannotation.entity.Person;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 这个BeanPostProcess的子类比较特殊
 *  它扩展了 BeanPostProcess 接口，并添加了新的功能
 *    1、【实例化前】的钩子函数，用来抑制默认的构造方法创建实例对象
 *    2、【实例化后属性填充前】的钩子函数，用来判断是否需要属性填充
 *    3、【属性填充时】的钩子函数，用来替换属性值
 *
 *
 * 它会在填充bean属性前进行调用，根据返回true/false来决定是否对这个bean进行属性填充
 * 详见  AbstractAutowireCapableBeanFactory.java:1340
 * @author liujianxin
 * @date 2021/4/5 17:18
 */
@Component
@Import({InstantiationAwareBeanPostProcessDemo.PopulationBean.class})
public class InstantiationAwareBeanPostProcessDemo implements InstantiationAwareBeanPostProcessor {
    
    /**
     * 【实例化前】被调用，此时对象还没被创建，一般用来抑制默认的构造函数创建对象
     *  默认返回null，如果返回了一个对象，则直接跳过了属性填充前、属性填充、初始化前、初始化的钩子方法，直接调用初始化后的钩子方法
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }
    
    
    
    
    /**
     * 在【实例化】后，属性填充前调用
     *
     * 如果返回true则进行属性填充，否则不填充
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if("populationBean".equals(beanName)){
            System.out.println("不对PopulationBean进行填充属性！");
            return false;
        }
        return true;
    }
    
    /**
     * 在对bean进行属性填充时的钩子方法
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
            throws BeansException {
        return null;
    }
    
    
    /**
     * 测试用子类
     * 验证 postProcessAfterInstantiation 方法返回false后不对这个bean设置属性
     */
    @Component("populationBean")
    public static class PopulationBean{
        @Autowired
        private Person person;
        
        @PostConstruct
        public void init(){
            System.out.println("PopulationBean的Person属性是否被填充："+person);
        }
    }
}
