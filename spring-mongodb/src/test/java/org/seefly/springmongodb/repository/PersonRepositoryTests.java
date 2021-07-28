package org.seefly.springmongodb.repository;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.wrapper.Persons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author liujianxin
 * @date 2021/7/28 17:43
 **/
@SpringBootTest
public class PersonRepositoryTests {
    
    @Autowired
    private PersonRepository personRepository;
    
    @Test
    void testPersonAgeIn(){
        List<Person> personByAgeIn = personRepository.findPersonByAgeIn(Arrays.asList(10, 11, 12, 13, 14, 15, 16));
        System.out.println(personByAgeIn);
    }
    
    @Test
    void testPersonNameLikeAndHeightBetween(){
        List<Person> a = personRepository.findPersonByNameLikeAndHeightBetween("a", 100, 120);
        System.out.println(a);
    }
    
    @Test
    void testPage(){
        List<Person> a = personRepository.findPersonByNameLike("a", PageRequest.of(1, 5));
        System.out.println(a.size());
    }
    
    @Test
    void testTop5(){
        
        Sort.TypedSort<Person> person = Sort.sort(Person.class);
        person.by(Person::getAge).descending();
        List<Person> than = personRepository.findTop5ByAgeGreaterThan(30,person);
        System.out.println(than);
    }
    
    @Test
    void testStreamable(){
        long start = System.currentTimeMillis();
        Optional<Person> a = personRepository.findPersonByNameContaining("a").peek(p -> {
            System.out.println(System.currentTimeMillis() - start);
        }).findFirst();
    }
    
    /**
     * 下面这种测试，数据量3000条的时候
     * Stream返回值类型的效率比List或者Streamable快3倍
     */
    @Test
    void testPerformanceBetweenStreamAndList(){
        long start = System.currentTimeMillis();
        Optional<Person> first = personRepository.findByAgeGreaterThan(0).stream().peek(p -> {
            System.out.println(System.currentTimeMillis() - start);
        }).findFirst();
    }
    
    @Test
    void testWrapper(){
        Persons result = personRepository.findPersonByAgeNotIn(Arrays.asList(1, 2, 3));
        System.out.println(result.totalAge());
    }
}
