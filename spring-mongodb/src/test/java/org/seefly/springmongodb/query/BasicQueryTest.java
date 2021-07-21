package org.seefly.springmongodb.query;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.seefly.springmongodb.entity.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liujianxin
 * @date 2021/7/20 15:24
 **/
public class BasicQueryTest extends BaseWithoutSpringTest {



    /**
     * select * from person where age < 50 and age > 40
     */
    @Test
    void testQueryByString(){
        List<Person> result = template.find(new BasicQuery("{ age : { $lt : 50 ,$gt: 40}}").with(Sort.by("age").descending()), Person.class);
        System.out.println(result.stream().map(Person::getAge).collect(Collectors.toList()));
    }

    @Test
    void testAndQuery(){
        // not work!!!
        List<Person> list = template.find(new BasicQuery("{age :{$gt: 30},$where :function() {return this.name.length > 9}}"), Person.class);
        System.out.println(list);
    }


}
