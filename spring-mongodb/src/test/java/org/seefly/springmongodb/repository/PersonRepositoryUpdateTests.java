package org.seefly.springmongodb.repository;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

/**
 * @author liujianxin
 * @date 2021/8/18 11:27
 */
@SpringBootTest
public class PersonRepositoryUpdateTests {
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testUpdate(){
        Optional<Person> byId = personRepository.findById("60f938f523f17a32c93a8b10");
        if(byId.isPresent()) {
            Person person = byId.get();
            person.setName("for update");
            person.setAge(null);
            personRepository.save(person);
        }
    }
}
