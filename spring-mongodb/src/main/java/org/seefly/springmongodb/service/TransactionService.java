package org.seefly.springmongodb.service;

import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liujianxin
 * @date 2021/8/17 17:08
 */
@Service
public class TransactionService {
    private final PersonRepository repository;

    public TransactionService(PersonRepository repository) {
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveTowPerson(Person p1,Person p2) {
        repository.save(p1);
        int a = 1 / 0;
        repository.save(p2);
    }
}
