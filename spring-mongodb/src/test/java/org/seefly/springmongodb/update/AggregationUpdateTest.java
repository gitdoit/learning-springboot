package org.seefly.springmongodb.update;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.seefly.springmongodb.entity.Person;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.aggregation.ScriptOperators;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newUpdate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author liujianxin
 * @date 2021/7/16 16:07
 **/
public class AggregationUpdateTest extends BaseWithoutSpringTest {



    @Test
    void testAggregation(){
       AggregationUpdate update = newUpdate(
               match(where("name").is("Michael"))
        ).set("height").toValue("121").set("name").toValueOf(ScriptOperators.Function.function("function(name) {return '66665';}").args("$name").lang("js"));
        template.update(Person.class).apply(update);

        // not work,don't use 'this' in MongoTemplate
      //template.updateFirst(query(where("name").is("Michael")), Update.update("name",ScriptOperators.Function.function("function(name) {return '66665';}").args("$name").lang("js")),Person.class);
    }
}
