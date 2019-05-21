package org.seefly.springbasic.parambind.component;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 演示使用ConfigurationProperties绑定参数
 * 这种类型的参数绑定适用于批量绑定操作，优点是支持松散绑定，JSR303数据校验，复杂类型封装(List\Map)
 *
 *
 * @author liujianxin
 * @date 2018-11-26 22:09
 */
@Data
@Component
@Validated
@ConfigurationProperties(prefix = "param.student")
public class BeanWithCfgPro {
    /**使用JSR303数据校验*/
    @Length(min = 1,max = 99)
    private String name;
    @Max(99)
    private Integer age;
    private Date birth;
    private List<String> hobbies;
    private Map<String,String> info;


}
