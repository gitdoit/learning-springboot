package org.seefly.quickstart.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liujianxin
 * @date 2018-06-09 19:57
 * 描述信息：通过COnfigurationProPerties注解来将配置文件中以domo.peopro为前缀的键值对注入到
 * 该类中对应的属性中
 **/
@Component
@ConfigurationProperties(prefix = "demo.peopro")
public class PeoPor {
    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "PeoPor{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
