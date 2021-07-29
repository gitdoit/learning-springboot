package org.seefly.springmongodb.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.repository.CustomizedRepository;
import org.springframework.data.mongodb.core.MongoTemplate;


/**
 *
 * The implementation itself does not depend on Spring Data and can be a regular Spring bean.
 * Consequently, you can use standard dependency injection behavior to inject references to other beans (such as a JdbcTemplate), take part in aspects, and so on.
 * @author liujianxin
 * @date 2021/7/29 10:21
 **/
@Slf4j
public class CustomizedRepositoryImpl implements CustomizedRepository<Person> {
    private final MongoTemplate template;
    
    public CustomizedRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }
    
    /**
     * 覆盖MongoRepository默认的方法
     * https://docs.spring.io/spring-data/mongodb/docs/3.2.3/reference/html/#repositories.query-streaming  看 9.6.1
     */
    @Override
    public Person save(Person entity) {
        log.info("自定义实现save，替换自带的save");
        return template.save(entity);
    }
    
   
}
