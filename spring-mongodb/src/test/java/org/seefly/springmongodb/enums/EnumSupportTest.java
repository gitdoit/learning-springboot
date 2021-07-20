package org.seefly.springmongodb.enums;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.utils.MongoClientUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author liujianxin
 * @date 2021/7/19 16:01
 **/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumSupportTest {

    MongoTemplate template;

    @BeforeAll
    void before() {
        template = MongoClientUtil.create("test");
    }

    @Test
    void testInsertWithEnum(){
        Person p = new Person();
        p.setAge(12);
        p.setName("enum");
        p.setStatus(AuditStatusEnum.AUDITING);

        template.save(p);
    }

    @Test
    void find(){
        List<Person> list = template.find(query(where("name").is("enum")), Person.class);
        System.out.println(list);
    }
}
