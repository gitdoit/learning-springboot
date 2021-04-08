package org.seefly.springannotation.circle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 演示循环依赖
 * A --> B
 * B --> A
 *
 * 对于构造函数的循环依赖，无法解决。
 *
 *
 * @author liujianxin
 * @date 2021/4/7 10:05
 */
@Configuration
@ComponentScan(basePackages = {"org.seefly.springannotation.circle"})
public class CircleApplicationDemoRun {
    
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CircleApplicationDemoRun.class);
        ABean bean = context.getBean(ABean.class);
        System.out.println(bean);
    }
    
}
