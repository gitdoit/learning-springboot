package org.seefly.springweb02.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 演示从自定义配置文件中加载属性
 * @author liujianxin
 * @date 2018-11-26 22:56
 */
@Data
@Component
@PropertySource(value = {"classpath:param.properties"})
@ConfigurationProperties(prefix = "peopro")
public class BeanWithProperties {
    private String name;
    private Integer age;
    private Date birth;
    private List<String> hobbies;
    private Map<String,String> info;
}
