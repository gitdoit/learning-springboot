package org.seefly.springannotation.lifecycle;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 通过JSR250中声明的初始化、销毁注解来管理生命周期
 * 触发机制
 *
 * 1. 属性填充
 *
 * 2. 初始化方法前钩子 BeanPostProcess.postProcessBeforeInitialization()
 *       ---> CommonAnnotationBeanPostProcessor.postProcessBeforeInitialization()
 *          ---> @PostConstruct 执行这个注解标注的初始化方法
 *
 * 3. 调用初始化方法 invokeInitMethods,处理Spring框架的初始化回调
 *
 * 4. 调用初始化方法后钩子 BeanPostProcess.postProcessAfterInitialization()
 *
 *
 *
 * @author liujianxin
 * @date 2018-12-26 21:17
 */
@Component
public class ByJSR {

    @PostConstruct
    private void init(){
        System.out.println("[ByJSR]JSR250--PostConstruct初始化方法被调用");
    }

    @PreDestroy
    private void destroy(){
        System.out.println("[ByJSR]JSR250--PreDestroy销毁方法被调用");
    }
}
