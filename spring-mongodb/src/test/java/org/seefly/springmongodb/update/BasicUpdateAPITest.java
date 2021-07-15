package org.seefly.springmongodb.update;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jdk.internal.util.EnvUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.MemberReadHistory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
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
        MongoClient client = MongoClients.create("mongodb://admin:"+ EnvUtils.getEnvVar("MY_PWD")+"@"+EnvUtils.getEnvVar("MY_SERVER")+":27017/test?authSource=admin");
        template = new MongoTemplate(client, "test");
    }
    
    @Test
    void testUpdateField(){
        template.updateFirst(query(where("productName").is("Iphone")),new Update().inc("accounts.$.balance", 50.00), MemberReadHistory.class);
    }
}
