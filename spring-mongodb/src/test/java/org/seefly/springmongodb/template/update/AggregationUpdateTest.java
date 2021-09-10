package org.seefly.springmongodb.template.update;

import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.seefly.springmongodb.entity.Person;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.aggregation.ScriptOperators;
import org.springframework.data.mongodb.core.aggregation.StringOperators;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newUpdate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * 坑爹的Navicat并不支持聚合管道更新的方式，只能登录到控制台执行更新语句
 *
 * 聚合管道(aggregation pipeline )更新被支持于 mongodb version >= 4.2
 *
 * @author liujianxin
 * @date 2021/7/16 16:07
 **/
public class AggregationUpdateTest extends BaseWithoutSpringTest {


    @Test
    void testUpdateAggregationPipeline(){
        AggregationUpdate update = newUpdate()
                .set("name").toValue(StringOperators.Substr.valueOf("name").substring(1,2));
        UpdateResult first = template.update(Person.class).matching(query(where("name").is("Boat_Cone"))).apply(update).first();
    }


    /**
     * 【利用函数更新某个字段】
     *  移除name字段里面所有的双引号
     * [{ "$set" : { "name" : { "$function" : { "body" : "function(name) {return name.replace(/\"/g,'');}", "args" : ["$name"], "lang" : "js"}}}}]
     */
    @Test
    void testAggregation(){
       AggregationUpdate update = newUpdate( )
               .set("name").toValueOf(ScriptOperators.Function.function("function(name) {return name.replace(/\"/g,'');}").args("$name").lang("js"));
        template.update(Person.class).apply(update).all();

        // not work, update one field from another ,should use aggregation pipeline update
       //template.updateFirst(query(where("name").is("Michael")), Update.update("name",ScriptOperators.Function.function("function(name) {return '66665';}").args("$name").lang("js")),Person.class);
    }


    @Test
    void updateHeightAsRandom(){
        AggregationUpdate update = newUpdate()
                .set("height").toValueOf(ScriptOperators.Function.function("function(name) {return Math.round(Math.random() * (200 - 100)) + 100;}").args("$name").lang("js"));
        template.update(Person.class).apply(update).all();
    }
}
