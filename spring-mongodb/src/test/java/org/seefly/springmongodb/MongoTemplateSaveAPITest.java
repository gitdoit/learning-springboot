package org.seefly.springmongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jdk.internal.util.EnvUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.MemberReadHistory;
import org.seefly.springmongodb.entity.NestedEntity;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 * @author liujianxin
 * @date 2021/7/9 10:13
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MongoTemplateSaveAPITest {
    
    MongoTemplate template;
    
    public static void main(String[] args) {
    }
    
    @BeforeAll
    void before() {
        
        MongoClient client = MongoClients.create("mongodb://admin:"+EnvUtils.getEnvVar("MY_PWD")+"@"+EnvUtils.getEnvVar("MY_SERVER")+":27017/test?authSource=admin");
        template = new MongoTemplate(client, "test");
    }
    
    @Test
    void testInsert(){
        NestedEntity entity = new NestedEntity();
        entity.setAge(12);
        entity.setName("Jean");
        
        NestedEntity.InnerClass inner = new NestedEntity.InnerClass();
        inner.setLength(123L);
        inner.setSource("OK~");
        entity.setValue(inner);
        
        template.save(entity);
    }
    
    @Test
    void testInsertEach(){
        // can saveAll instead
        for (int i = 0; i < 20; i++) {
            MemberReadHistory entity = new MemberReadHistory();
            entity.setMemberId(1L);
            entity.setMemberNickname("Michael");
            entity.setMemberIcon("string");
            entity.setProductName("Iphone"+i);
            entity.setProductId((long) i);
            entity.setProductPic("string");
            entity.setProductSubTitle("string");
            entity.setCreateTime(new Date());
            template.save(entity);
        }
    }
    
    
}
