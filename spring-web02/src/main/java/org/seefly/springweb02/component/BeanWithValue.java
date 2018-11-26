package org.seefly.springweb02.component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * {@link Value}不支持复杂类型封装，也就是List，Map这种,也不支持JSR303数据校验
 * 但是它支持spEL表达式
 * @author liujianxin
 * @date 2018-11-26 22:32
 */
@Data
@Component
public class BeanWithValue {
    @Value("${param.student.name}")
    private String name;
    @Value("${param.student.age}")
    private Integer age;
    @Value("${param.student.birth}")
    private Date birth;
    @Value("#{11*2}")
    private Integer spEL;
}
