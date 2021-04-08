package org.seefly.springbasic.event;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 和CommandLineRunner用法相同，在程序启动容器完全可用之后收到回调
 * @author liujianxin
 * @date 2021/4/8 10:16
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner回调");
    }
}
