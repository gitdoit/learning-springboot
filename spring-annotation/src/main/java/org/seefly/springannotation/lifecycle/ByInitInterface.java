package org.seefly.springannotation.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 实现这两个接口也可以代替@Bean注解种的初始化销毁参数
 * 我觉得这个弥补某些bean不是用注解方式注入容器，但要实现初始化、销毁逻辑。就像是用@Import
 *
 *
 *
 * 1. 属性填充
 *
 * 2. 初始化方法前钩子 BeanPostProcess.postProcessBeforeInitialization()
 *
 * 3. 调用初始化方法 invokeInitMethods
 *                     ---> InitializingBean.afterPropertiesSet() 就是这里触发
 *
 * 4. 调用初始化方法后钩子 BeanPostProcess.postProcessAfterInitialization()
 *
 *
 * @author liujianxin
 * @date 2018-12-23 21:57
 */
@Component
public class ByInitInterface implements InitializingBean, DisposableBean {


    /**
     * InitializingBean接口方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("[ByInitInterface]InitializingBean.afterPropertiesSet被调用");
    }
    @Override
    public void destroy() throws Exception {
        System.out.println("[ByInitInterface]DisposableBean.destroy被调用");
    }

}
