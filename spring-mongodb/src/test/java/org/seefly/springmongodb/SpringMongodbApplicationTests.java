package org.seefly.springmongodb;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.entity.MemberReadHistory;
import org.seefly.springmongodb.repository.MemberReadHistoryRepository;
import org.seefly.springmongodb.service.MemberReadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SpringMongodbApplicationTests {
    @Autowired
    private MemberReadHistoryRepository readHistoryRepository;
    @Autowired
    private MemberReadHistoryService service;
    
    @Test
    void contextLoads() {
    
    
    }
    
    @Test
     void testFindByProductId(){
        List<MemberReadHistory> list = readHistoryRepository
                .findMemberReadHistoryByProductId(2L);
        System.out.println(list);
    }
    
    @Test
    void testFindByProductIdWithTemplate(){
        Optional<MemberReadHistory> byProductId = service.findByProductId(2L);
        MemberReadHistory memberReadHistory = byProductId.get();
        System.out.println(memberReadHistory);
    }
    
}
