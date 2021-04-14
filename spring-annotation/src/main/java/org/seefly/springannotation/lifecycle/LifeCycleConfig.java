package org.seefly.springannotation.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * bean的生命周期学习。
 * 注解方式
 *  1、注解方式向容器中注入组件可以通过 initMethod和destroyMethod方法指定在bean创建和销毁时执行的方法
 *  2、对于单实例bean由于默认不是懒加载，所以在容器初始化的时候就会调用一次init方法，对于多实例则只有在调用的时候才会执行一次init
 *  3、对于单实例bean，在容器销毁的时候会执行一次destroy方法，但是对于多实例bean则不会调用销毁方法。
 *
 *  实现接口方式
 *  1、bean通过实现{@link InitializingBean}接口来指定在实例初始化时候的逻辑
 *  2、bean通过实现{@link DisposableBean}接口来指定实例在销毁时的逻辑
 *
 *  JSR250注解方式
 *  1、{@link PostConstruct} 初始化
 *  2、{@link PreDestroy} 销毁  这俩都是javax中的，不是spring的
 *
 *  实现
 * @author liujianxin
 * @date 2018-12-23 21:36
 */
@Configuration
@ComponentScan(basePackages = {"org.seefly.springannotation.lifecycle"})
public class LifeCycleConfig {

    @Bean(initMethod = "init",destroyMethod = "destroy")
    public ByAnnotationParam tree(){
        return new ByAnnotationParam();
    }

}
