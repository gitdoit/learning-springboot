package org.seefly.springmongodb.query;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jdk.internal.util.EnvUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.Person;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * <a href="https://docs.mongoing.com/mongodb-crud-operations/query-documents/query-an-array">中文文档-集合操作</a>
 * <a href="https://docs.mongodb.com/v4.2/reference/operator/query/">操作符</a>
 * @author liujianxin
 * @date 2021/7/15 21:43
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionQueryTest {
    MongoTemplate template;
    @BeforeAll
    void before() {
        MongoClient client = MongoClients
                .create("mongodb://admin:"+ EnvUtils.getEnvVar("MY_PWD")+"@"+EnvUtils.getEnvVar("MY_SERVER")+":27017/test?authSource=admin");
        template = new MongoTemplate(client, "test");
    }
    
    /**
     * 包含一个
     *
     * 查询 hobbies集合中有一个 "play" 元素的记录
     */
    @Test
    void hasA(){
        List<Person> people = template.find(query(where("hobbies").is("play")), Person.class);
        System.out.println(people);
    }
    
    /**
     * 同时包含
     *
     * 查找一条记录A,A的hobbies属性中,必须同时包含 play 和 eat两个元素
     */
    @Test
    void hasAll(){
        List<Person> people = template.find(query(where("hobbies").all("play", "eat")), Person.class);
        System.out.println(people);
    }
    
    /**
     * 至少包含 any of ...
     *
     * 查找一条记录A,A的hobbies属性中,要么包含 play元素  要么包含 eat元素
     * 一般用在条件筛选,例如筛选爱好是 play的或者爱好是eat的用户
     */
    @Test
    void hasAny(){
        List<Person> people = template.find(query(where("hobbies").elemMatch(where("$in").is(Arrays.asList("play","eat")))), Person.class);
        System.out.println(people.size());
    }
    
    /**
     * 一个都不能包含
     * 查找一条记录A,A的hobbies属性中,不能包含 eat 也不能包含 drink
     */
    @Test
    void hasNoOneOf(){
        List<Person> people = template.find(query(where("hobbies").elemMatch(where("$nin").is(Arrays.asList("drink","eat")))), Person.class);
        System.out.println(people.size());
    }
    
    @Test
    void sizeGreaterThan(){
        List<Person> people = template.find(query(where("hobbies")), Person.class);
        System.out.println(people.size());
    }
    
    
}
