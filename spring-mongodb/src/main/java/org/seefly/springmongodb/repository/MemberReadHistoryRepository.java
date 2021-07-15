package org.seefly.springmongodb.repository;

import org.seefly.springmongodb.entity.MemberReadHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author liujianxin
 * @date 2021/7/7 19:25
 */
public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory, String> {
    List<MemberReadHistory> findMemberReadHistoryByProductId(Long productId);
}
