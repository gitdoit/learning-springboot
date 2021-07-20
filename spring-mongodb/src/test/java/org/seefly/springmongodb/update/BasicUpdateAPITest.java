package org.seefly.springmongodb.update;

import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.MemberReadHistory;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.utils.MongoClientUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author liujianxin
 * @date 2021/7/9 10:26
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BasicUpdateAPITest {
    MongoTemplate template;
    
    @BeforeAll
    void before() {
        template = MongoClientUtil.create("test");
    }
    
    @Test
    void testUpdateField(){
        template.updateFirst(query(where("productName").is("Iphone")),new Update().inc("accounts.$.balance", 50.00), MemberReadHistory.class);
    }

    @Test
    void testUpdate(){
    }

    @Test
    void testUpdateVersion(){
        UpdateResult updateResult = template.updateFirst(query(where("_id").is("60f669b4aa4dab5039e775f2")),
                new Update().set("memberNickname", "1232").set("version",6), MemberReadHistory.class);
        System.out.println(updateResult);
    }
}
