package org.seefly.transaction.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujianxin
 * @date 2019-05-21 14:57
 */
@MapperScan("org.seefly.transaction.mapper")
@Configuration
public class DbConfig {
}
