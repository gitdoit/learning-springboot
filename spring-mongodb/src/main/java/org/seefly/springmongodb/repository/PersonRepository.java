package org.seefly.springmongodb.repository;

import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.wrapper.Persons;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.stream.Stream;

/**
 *
 * 注意,对与条多条结果的返回值类型
 *  1、List 最常见的，所有结果都从数据库中取出来的时候才会返回
 *  2、Streamable Spring data包装的，用来方便的处理可迭代的集合
 *  3、Stream 流式数据，这代表着当第一条数据被返回的时候就已经开始处理了，而不必等到所有数据都从数据库返回
 *
 * @author liujianxin
 * @date 2021/7/28 16:44
 **/
public interface PersonRepository extends MongoRepository<Person,String> {

    List<Person> findPersonByAgeIn(List<Integer> ages);
    
    List<Person> findPersonByNameOrAge(String name,Integer age);
    
    List<Person> findPersonByNameLikeAndHeightBetween(String name,Integer low,Integer hei);
    
    List<Person> findPersonByNameLike(String start, Pageable pageable);
    
    List<Person> findTop5ByAgeGreaterThan(Integer age, Sort sort);
    
    Stream<Person> findPersonByNameContaining(String name);
    
    Streamable<Person> findByAgeGreaterThan(Integer age);
    
    
    Persons findPersonByAgeNotIn(List<Integer> ages);
    

}
