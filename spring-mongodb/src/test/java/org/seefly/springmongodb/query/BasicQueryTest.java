package org.seefly.springmongodb.query;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.seefly.springmongodb.entity.Person;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liujianxin
 * @date 2021/7/20 15:24
 **/
public class BasicQueryTest extends BaseWithoutSpringTest {

    @Test
    void testQueryByString(){
        BasicQuery query = new BasicQuery("{ age : { $lt : 50 ,$gt: 40}}");
        List<Person> result = template.find(query, Person.class);
        System.out.println(result.stream().map(Person::getAge).collect(Collectors.toList()));

    }
}
