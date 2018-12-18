package org.seefly.springannotation.config;

import org.seefly.springannotation.entity.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author liujianxin
 * @date 2018-12-18 23:55
 */
@Configuration
public class MainConfig {

    @Bean
    public Person person(){
        return new Person("liu",12);
    }


}
