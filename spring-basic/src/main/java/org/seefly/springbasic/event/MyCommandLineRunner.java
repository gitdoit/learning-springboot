package org.seefly.springbasic.event;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * 应用程序已经启动之后会调用这个接口的回调方法
 * 一般用来在应用程序启动后做一些操作
 * 比如我之前遇到的，在程序启动之后各种数据库连接都可用了，然后读取数据库加载一些信息到内存里，就可以用这个方法
 *
 *
 * @author liujianxin
 * @date 2021/4/8 10:12
 */
@Component
public class MyCommandLineRunner implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner回调");
    }
}
