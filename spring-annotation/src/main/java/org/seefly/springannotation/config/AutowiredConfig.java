package org.seefly.springannotation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 *
 * @author liujianxin
 * @date 2019/9/15 16:03
 */
@Configuration
@ComponentScan(basePackages = {"org.seefly.springannotation.entity.autowired"})
public class AutowiredConfig {

}
