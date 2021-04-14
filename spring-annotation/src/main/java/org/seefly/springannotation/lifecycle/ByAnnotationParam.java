package org.seefly.springannotation.lifecycle;

/**
 * 用来演示bean的生命周期
 * 在@Bean注解的参数中指定该实例的初始化和销毁方法
 * 1. 属性填充
 *
 * 2. 初始化方法前钩子 BeanPostProcess.postProcessBeforeInitialization()
 *
 * 3. 调用初始化方法 invokeInitMethods
 *                     ---> ByAnnotationParam.init() 就是这里触发
 *
 * 4. 调用初始化方法后钩子 BeanPostProcess.postProcessAfterInitialization()
 *
 * @author liujianxin
 * @date 2018-12-23 21:38
 */
public class ByAnnotationParam {



    public void init(){
        System.out.println("[ByAnnotationParam]@Bean(init='') 初始化方法被调用");
    }

    public void destroy(){
        System.out.println("[ByAnnotationParam]@Bean(destroyMethod='') 销毁方法被调用");
    }
}
