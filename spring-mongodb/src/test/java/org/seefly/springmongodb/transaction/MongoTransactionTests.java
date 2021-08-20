package org.seefly.springmongodb.transaction;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author liujianxin
 * @date 2021/8/17 17:12
 */
@SpringBootTest
public class MongoTransactionTests{
    @Autowired
    private TransactionService transactionService;


    @Test
    public void testTransaction() {
        Person p = new Person();
        p.setAge(18);
        p.setName("aaaa");
        p.setHeight(188);
        p.setHobbies(Arrays.asList("eat","play","dance","run"));

        Person p1 = new Person();
        p1.setAge(19);
        p1.setName("bbb");
        p1.setHeight(155);
        p1.setHobbies(Arrays.asList("eat","play","dance","run"));

        transactionService.saveTowPerson(p1,p);
    }
}
