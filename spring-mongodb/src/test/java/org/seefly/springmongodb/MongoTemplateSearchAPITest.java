package org.seefly.springmongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.MemberReadHistory;
import org.seefly.springmongodb.entity.NestedEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author liujianxin
 * @date 2021/7/8 17:25
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MongoTemplateSearchAPITest {
    MongoTemplate template;
    
    @BeforeAll
    void before() {
        MongoClient client = MongoClients.create("mongodb://admin:shang2010@121.36.142.5:27017/test?authSource=admin");
        template = new MongoTemplate(client, "test");
    }
    
    /**
     * 无条件查询一个
     * select * from memberReadHistory limit 1
     */
    @Test
    void findOne(){
        MemberReadHistory one = template.findOne(new Query(), MemberReadHistory.class);
        System.out.println(one);
    }
    
    /**
     * 模糊查询
     * select * from memberReadHistory where productName like %y%
     */
    @Test
    void fuzzyQuery(){
        Criteria criteria = new Criteria();
        criteria.and("productName").regex("y");
        List<MemberReadHistory> memberReadHistories = template.find(new Query(criteria), MemberReadHistory.class);
        System.out.println(memberReadHistories);
    }
    
    /**
     * 在...之间
     * select * from memberReadHistory where productId productId < 6 and productId > 3
     */
    @Test
    void findBetween(){
        Criteria criteria = new Criteria();
        criteria.and("productId").lt(6).gt(3);
        List<MemberReadHistory> list = template.find(new Query(criteria), MemberReadHistory.class);
        System.out.println(list);
    }
    
    /**
     * 正则匹配,以...结尾
     * select * from memberReadHistory where productName like %3
     */
    @Test
    void findEndWith(){
        Criteria criteria = new Criteria();
        criteria.and("productName").regex(".*?3$");
        List<MemberReadHistory> memberReadHistories = template.find(new Query(criteria), MemberReadHistory.class);
        System.out.println(memberReadHistories);
    }
    
    /**
     * in
     * select * from memberReadHistory where productId in (1,2,3)
     */
    @Test
    void testIn(){
        Criteria criteria = new Criteria();
        criteria.and("productId").in(1,2,3);
        List<MemberReadHistory> memberReadHistories = template.find(new Query(criteria), MemberReadHistory.class);
        System.out.println(memberReadHistories);
    }
    
    /**
     * select * from memberReadHistory where productId in (1,2,3) or productName like '%6'
     */
    @Test
    void testAndOr(){
        Criteria conditions = new Criteria();
        conditions.orOperator(Criteria.where("productId").in(1,2,3),Criteria.where("productName").regex(".*?6$"));
        List<MemberReadHistory> memberReadHistories = template.find(new Query(conditions), MemberReadHistory.class);
        System.out.println(memberReadHistories);
    }
    
    @Test
    void findNested(){
        NestedEntity one = template.findOne(new Query(), NestedEntity.class);
        System.out.println(one);
    }
    
    
    
    
}
