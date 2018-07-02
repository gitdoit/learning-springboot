package org.seefly.quickstart.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2018-06-09 19:57
 * 描述信息：通过COnfigurationProPerties注解来将配置文件以domo.peopro为前缀的键值对注入到
 * 该类中对应的属性中
 **/
//@Validated

/**
 * 使用下面这个注解可以指定读取指定的配置文件进行注入
 * @PropertySource
  */
@PropertySource(value = {"classpath:peopor.properties"})
@ConfigurationProperties(prefix = "peopro")
@Component
@Data
public class PeoPor {
    /**
     * 使用校验器校验属性注入的值是否合法，
     * 不过 seefly@ 这个写法都能通过校验，坑爹
     */
    //@Email
    private String name;
    private String age;
    private List<String> list;
    private Map<String,String> map;
    private Date date;
    private Dog dog;


}
