package org.seefly.springannotation.lifecycle;

/**
 * 用来演示bean的生命周期
 * 在@Bean注解的参数中指定该实例的初始化和销毁方法
 * @author liujianxin
 * @date 2018-12-23 21:38
 */
public class ByAnnotationParam {

    public ByAnnotationParam() {
        System.out.println("我种了一棵树.....");
    }


    public void init(){
        System.out.println("开始给小树苗浇水....");
    }

    public void destroy(){
        System.out.println("水浇多了，小树苗死了....");
    }
}
