package org.seefly.springmongodb.repository.extents;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author liujianxin
 * @date 2021/8/23 10:27
 */
@SpringBootTest
public class ExtendsSupportTest {

    @Autowired
    private PersonRepository personRepository;


    @Test
    public void tenantQuery() {
        List<Person> persons = personRepository.findByHeightBetween(120, 145);

        System.out.println(persons);
    }
}
