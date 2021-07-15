package org.seefly.springmongodb.service;

import org.seefly.springmongodb.entity.MemberReadHistory;
import org.seefly.springmongodb.repository.MemberReadHistoryRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author liujianxin
 * @date 2021/7/7 19:28
 */
@Service
public class MemberReadHistoryService {
    private final MemberReadHistoryRepository readHistoryRepository;
    private final MongoTemplate mongoTemplate;
    
    public MemberReadHistoryService(MemberReadHistoryRepository readHistoryRepository, MongoTemplate mongoTemplate) {
        this.readHistoryRepository = readHistoryRepository;
        this.mongoTemplate = mongoTemplate;
    }
    
    public Optional<MemberReadHistory> findByProductId(Long productId){
        return Optional.ofNullable(mongoTemplate
                .findOne(new Query(new Criteria().and("productId").is(productId)), MemberReadHistory.class));
    }
    
    public void creatThree(){
    
    }
}
