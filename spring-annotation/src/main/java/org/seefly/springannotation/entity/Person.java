package org.seefly.springannotation.entity;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2018-12-18 23:54
 */
@Data
public class Person {
    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
