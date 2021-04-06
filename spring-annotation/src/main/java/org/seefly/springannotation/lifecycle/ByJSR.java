package org.seefly.springannotation.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 通过JSR250中声明的初始化、销毁注解来管理生命周期
 *
 * @author liujianxin
 * @date 2018-12-26 21:17
 */
public class ByJSR {

    @PostConstruct
    private void init(){
        System.out.println("JSR250--PostConstruct注解生效，初始化...");
    }

    @PreDestroy
    private void destroy(){
        System.out.println("JSR250--PreDestroy注解生效，销毁...");
    }
}
