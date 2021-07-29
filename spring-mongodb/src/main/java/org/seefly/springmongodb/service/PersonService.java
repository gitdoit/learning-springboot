package org.seefly.springmongodb.service;

import lombok.extern.slf4j.Slf4j;
import org.seefly.springmongodb.entity.event.DomainEvent;
import org.seefly.springmongodb.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author liujianxin
 * @date 2021/7/28 16:46
 **/
@Slf4j
@Service
public class PersonService {
    
    private final PersonRepository repository;
    
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void domainEventListener(DomainEvent event){
        System.out.println(event);
    }
}
