package org.seefly.springmongodb.repository;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.projections.DynamicProjections;
import org.seefly.springmongodb.projections.NameHobby;
import org.seefly.springmongodb.wrapper.Persons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * @author liujianxin
 * @date 2021/7/28 17:43
 **/
@SpringBootTest
public class PersonRepositoryQueryTests {
    
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
    
    /**
     * 	A Stream potentially wraps underlying data store-specific resources and must, therefore, be closed after usage.
     * 	You can either manually close the Stream by using the close() method or by using a Java 7 try-with-resources block
     */
    @Test
    void testStreamable(){
        long start = System.currentTimeMillis();
        try(Stream<Person> stream = personRepository.findPersonByNameContaining("a")){
            Optional<Person> first = stream.peek(p -> {
                System.out.println(System.currentTimeMillis() - start);
            }).findFirst();
        }
        
    }
    
    /**
     * 下面这种测试，数据量3000条的时候
     * Stream返回值类型的效率比List或者Streamable快3倍
     *
     * https://stackoverflow.com/questions/63115831/spring-data-repository-list-vs-stream
     */
    @Test
    void testPerformanceBetweenStreamAndList(){
        long start = System.currentTimeMillis();
        // 手动把 findByAgeGreaterThan的返回值换成stream或者List或者Streamable
        // 比较一下三者之间的区别
        Optional<Person> first = personRepository.findByAgeGreaterThan(0).stream().peek(p -> {
            System.out.println(System.currentTimeMillis() - start);
        }).findFirst();
    }
    
    /**
     * 返回包装类，这样可以附带一些逻辑
     */
    @Test
    void testWrapper(){
        Persons result = personRepository.findPersonByAgeNotIn(Arrays.asList(1, 2, 3));
        System.out.println(result.totalAge());
    }
    
    /**
     * 测试配合@Async异步返回结果
     */
    @Test
    void testAsync() throws ExecutionException, InterruptedException {
        
        long strat = System.currentTimeMillis();
        Future<Person> fu = personRepository.findTopByName("Toolbox_Soap");
        System.out.println("立即返回:"+(System.currentTimeMillis() - strat));
        Person person = fu.get();
        System.out.println("等待完毕:"+(System.currentTimeMillis() - strat));
    }
    
    /**
     * 覆盖默认提供的save方法
     */
    @Test
    void testOverrideMethod() throws InterruptedException {
        Person p = new Person();
        p.setAge(34);
        p.setName("Michael jackson");
        p.setHeight(188);
        p.setHobbies(Arrays.asList("eat","play","dance","run"));
        // 这里的save走的不再是Spring Data Mongo提供的默认的了
        // 而是自己覆盖的 org.seefly.springmongodb.repository.impl.CustomizedRepositoryImpl.save
        personRepository.save(p);
        
        Thread.sleep(1000 * 10);
    }
    
    
    /**
     * 测试@Query注解写查询语句
     */
    @Test
    void testQuery(){
       try ( Stream<Person> stream = personRepository.findByTheAgeAndNameIs("Michael jackson", 34)){
           int sum = stream.mapToInt(Person::getAge).sum();
           System.out.println(sum);
       }
    }
    
    /**
     * 测试@Query注解写查询语句 + SpEl
     */
    @Test
    void testSpelQuery(){
        List<Person> bySpEl = personRepository.findBySpEl(20);
        System.out.println(bySpEl.size());
    }
    
    /**
     * 测试@Query注解写查询语句 + SpEl + 执行上下文
     */
    @Test
    void testSpelContextQuery(){
        List<Person> user = personRepository.findByContextUser();
        System.out.println(user);
    }
    
    /**
     * 测试@Query注解写查询语句 + SpEl + 执行上下文 + 方法调用
     */
    @Test
    void testSpElAndFunction(){
        List<Person> function = personRepository.findBySpElAndUseFunction();
        System.out.println(function);
    }
    
    
    /**
     * 只查询指定字段
     */
    @Test
    void testSelectSpecifiedField(){
        List<NameHobby> names = personRepository.findTop3ByName("Michael jackson");
        NameHobby one = names.get(0);
        System.out.println(one);
    
        List<Person> top2 = personRepository.findTop2ByName("Michael jackson");
        System.out.println(top2);
    }
    
    
    @Test
    void testDynamicProjections(){
        List<DynamicProjections.OnlyAge> onlyAges = personRepository
                .findTop6ByName("Michael jackson", DynamicProjections.OnlyAge.class);
        System.out.println(onlyAges);
    
        List<DynamicProjections.OnlyName> onlyNames = personRepository
                .findTop6ByName("Michael jackson", DynamicProjections.OnlyName.class);
        System.out.println(onlyNames);
    }
    
    
    
    
}
