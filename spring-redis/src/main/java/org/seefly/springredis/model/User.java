package org.seefly.springredis.model;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2018-09-05 10:02
 */
@Data
public class User {
    private String id;
    private String name;
    private Integer age;

    public User(){}

    public User(String id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
