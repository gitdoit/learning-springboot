package org.seefly.springannotation.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 实现这两个接口也可以代替@Bean注解种的初始化销毁参数
 * 我觉得这个弥补某些bean不是用注解方式注入容器，但要实现初始化、销毁逻辑。就像是用@Import
 * InitializingBean --> afterPropertiesSet
 * @author liujianxin
 * @date 2018-12-23 21:57
 */
public class ByInterface implements InitializingBean, DisposableBean {

    public ByInterface() {
        System.out.println("我种了一朵花....");
    }

    /**
     * InitializingBean接口方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("给小花浇水...");
    }
    @Override
    public void destroy() throws Exception {
        System.out.println("小花被狗吃了....");
    }

}
