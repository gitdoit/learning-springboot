package org.seefly.springmongodb.template.update;

import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.seefly.springmongodb.entity.MemberReadHistory;
import org.seefly.springmongodb.entity.Person;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author liujianxin
 * @date 2021/7/9 10:26
 */
public class BasicUpdateAPITest extends BaseWithoutSpringTest {




    /**
     * 【自增更新】
     * update person set height = height + 15 where name = Dog_Cat
     * { "name" : "Dog_Cat"} and update: { "$inc" : { "height" : 15}}
     */
    @Test
    void testUpdateField(){
        template.updateFirst(query(where("name").is("Dog_Cat")),new Update().inc("height", 15), Person.class);
    }


    @Test
    void testUpdateVersion(){
        UpdateResult updateResult = template.updateFirst(
                query(where("_id").is("60f669b4aa4dab5039e775f2")),
                new Update().set("memberNickname", "1232").set("version",6),
                MemberReadHistory.class
        );
        System.out.println(updateResult);
    }

    /**
     * 批量更新
     */
    @Test
    void updateForRandom(){




    }


}
