package org.seefly.springweb02.component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * {@link Value}不支持复杂类型封装，也就是List，Map这种,也不支持JSR303数据校验
 * 但是它支持spEL表达式
 * SPeL表达式用于获取容器中某个bean的属性或者调用它的方法
 * 引用属性：@Value("#{bean.attr}")
 * 调用方法:@Value("#{bean.returnSomething()}")
 *
 * 这种形式用来获取配置文件中的值 @Value("${configFile.attr}")
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

    /**
     * 使用SPeL表达式调用容器中bean的方法注入值
     */
    @Autowired
    public void setByValue(@Value("#{demoBean.showValue()}") String value){
        System.out.println(value);
    }
}
