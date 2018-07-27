package org.seefly.quickstart.config;

import org.seefly.quickstart.service.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujianxin
 * @date 2018-06-16 22:04
 * 描述信息：spring推荐使用代码配置的方式代替xml
 * @Configuration声明这是一个配置类
 * @Bean代替xmlz中的<bean><bean/>标签，作用在方法上
 * 方法名就是这个bean的id，返回值就是需要向容器中注入的组件，需要手动new
 * 这些注解都是spring的东西
 **/
@Configuration
public class MyAppConfig {



    @Bean
    public HelloService helloService(){
        System.out.println("使用代码配置向容器中注入HelloService...");
        return  new HelloService();
    }

}
