package org.seefly.springmongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author liujianxin
 * @date 2021/7/15 21:09
 */
@Data
@Document
public class Person {
    
    @Id
    private String id;
    
    private String name;
    
    private Integer height;
    
    private Integer age;
    
    private List<String> hobbies;
    
    private List<Person> children;

    private List<Integer> scores;
    
}
