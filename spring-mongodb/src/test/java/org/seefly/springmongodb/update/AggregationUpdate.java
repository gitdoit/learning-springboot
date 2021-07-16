package org.seefly.springmongodb.update;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.utils.MongoClientUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.valueOf;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Switch.CaseOperator.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author liujianxin
 * @date 2021/7/16 16:07
 **/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AggregationUpdate {
    MongoTemplate template;

    @BeforeAll
    void before() {
        template = MongoClientUtil.create("test");
    }


    @Test
    void testAggregation(){

      template.updateFirst(query(where("name").regex("^\"")),Update.update("name","this.name +1"),Person.class);
    }
}
