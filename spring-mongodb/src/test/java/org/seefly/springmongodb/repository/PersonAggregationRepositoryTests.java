package org.seefly.springmongodb.repository;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.entity.aggregate.PersonAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author liujianxin
 * @date 2021/7/29 18:36
 **/
@SpringBootTest
public class PersonAggregationRepositoryTests {
    
    @Autowired
    private PersonRepository personRepository;
    
    @Test
    void testGroupByAge(){
        List<PersonAggregate> results = personRepository.groupByAgeAndGreaterThen(20);
        System.out.println(results.size());
        System.out.println(results);
    }
    
    @Test
    void testGroupAndSum(){
        Long aLong = personRepository.sumAgeThatGreaterThen(15);
        System.out.println(aLong);
    }
    
}
