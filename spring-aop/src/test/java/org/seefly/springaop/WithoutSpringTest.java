package org.seefly.springaop;

import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

/**
 * @author liujianxin
 * @date 2021/4/2 16:02
 */
public class WithoutSpringTest {
    
    public static void main(String[] args) {
    
//        Map<String, String> getenv = System.getenv();
//        System.out.println(getenv);
        Properties properties = System.getProperties();
        System.out.println(properties);
    }
    
}
