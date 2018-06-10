package org.seefly.quickstart.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liujianxin
 * @date 2018-06-09 19:04
 * 描述信息：演示属性值的注入，这个跟以前spring一样都
 **/
@Component
public class Peo {
    @Value("${demo.name}")
    private String name;
    @Value("${demo.age}")
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
        return "Peo{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
