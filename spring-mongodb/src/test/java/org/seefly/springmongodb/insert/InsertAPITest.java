package org.seefly.springmongodb.insert;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.MemberReadHistory;
import org.seefly.springmongodb.entity.NestedEntity;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.utils.MongoClientUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * @author liujianxin
 * @date 2021/7/9 10:13
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InsertAPITest {
    
    MongoTemplate template;
    private static final String[] HOBBIES = new String[]{"eat","drink","play","happy","run","sleep","yellow"};

    
    @BeforeAll
    void before() {
        template = MongoClientUtil.create("test");
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
    
    
    @Test
    void savePerson() {
        Person p = new Person();
        p.setAge(34);
        p.setName("Michael");
        p.setHeight(188);
        p.setHobbies(Arrays.asList("eat","play","dance","run"));
        List<Person> children = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Person c = new Person();
            c.setName("child-"+i);
            c.setAge(i+1);
            c.setHeight(15*(i+1));
            c.setHobbies(Arrays.asList("play"));
            children.add(c);
        }
        p.setChildren(children);
        template.save(p);
    }

    @Test
    void makeData() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String names = restTemplate.getForObject("http://names.drycodes.com/1000", String.class).replace("[","").replace("]","");
        String[] split = names.split(",");
        List<Person> list = new ArrayList<>();
        Random random = new Random(21);
        for (int i = 0; i < split.length; i++) {
            String name = split[i];
            Person p = new Person();
            list.add(p);
            p.setAge(Math.abs(random.nextInt() % 100));
            p.setName(name.replaceAll("\"",""));
            p.setHeight(Math.abs(random.nextInt() % 100) + 100);
            p.setHobbies(someHobbies(i));
        }
        template.insertAll(list);




    }



    private List<String> someHobbies(int seed){
        Random random = new Random(seed);
        int count = Math.abs(random.nextInt() % 7);
        Set<String> names = new HashSet<>();
        for (int i = 0; i < count; i++) {
            names.add(HOBBIES[Math.abs(random.nextInt() % 6)]);
        }
        return new ArrayList<>(names);
    }

}
