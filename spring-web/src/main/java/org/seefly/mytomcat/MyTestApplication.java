package org.seefly.mytomcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author liujianxin
 * @date 2021/5/26 9:02
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MyTestApplication {
    public static void main(String[] args) {
        //SpringApplication.run(MyTestApplication.class, args);
       String base = "http://192.168.10.120:8082/GisqPlatformDesigner-Rest/sys/getUserByNameJoinDeptByPost";
        System.out.println(base.substring(0,base.indexOf("GisqPlatformDesigner-Rest")));
    }
}
