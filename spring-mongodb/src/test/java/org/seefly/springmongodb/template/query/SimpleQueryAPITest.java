package org.seefly.springmongodb.template.query;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.MemberReadHistory;
import org.seefly.springmongodb.entity.NestedEntity;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.utils.MongoClientUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author liujianxin
 * @date 2021/7/8 17:25
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleQueryAPITest {
    MongoTemplate template;


    @BeforeAll
    void before() {
        template = MongoClientUtil.create("test");
    }

    /**
     * 【查询指定字段】
     * 如果查询的数据量比较大，这个时候最好只查询指定的字段，减少数据量，避免OOM
     * query.fields().include(xx)只查询指定的字段(默认包含id),通过exclude()把id也剔除
     */
    @Test
    void findSpecifiedField(){
        Query query = query(where("age").lte(40));
        query.fields().include("age","name").exclude("id");
        Person one = template.findOne(query, Person.class);

        assert one != null;
        assert one.getChildren() == null;
        assert one.getId() == null;
        System.out.println(one);
    }


    /**
     * 【无条件查询一个】
     * select * from memberReadHistory limit 1
     */
    @Test
    void findOne(){
        MemberReadHistory one = template.findOne(new Query(), MemberReadHistory.class);
        System.out.println(one);
    }
    
    /**
     * 【模糊查询】
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
     * 【在...之间】
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
     * 【正则匹配,以...结尾】
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
     * 【in】
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
        conditions.orOperator(where("productId").in(1,2,3), where("productName").regex(".*?6$"));
        List<MemberReadHistory> memberReadHistories = template.find(new Query(conditions), MemberReadHistory.class);
        System.out.println(memberReadHistories);
    }
    
    @Test
    void findNested(){
        NestedEntity one = template.findOne(new Query(), NestedEntity.class);
        System.out.println(one);
    }
    
    
    
    
}
