package org.seefly.springmongodb.aggregation;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.utils.MongoClientUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author liujianxin
 * @date 2021/7/16 11:19
 **/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AggregationQuery {
    MongoTemplate template;

    @BeforeAll
    void before() {
        template = MongoClientUtil.create("test");
    }

    /**
     * 统计表中所有的年龄之和
     * select sum(age) from person
     */
    @Test
    void sumAllAge(){
        Aggregation aggregation = Aggregation.newAggregation(
                group().sum("age").as("age"),
                project("age")
        );
        Document results = template.aggregate(aggregation, "person",Document.class).getUniqueMappedResult();
        System.out.println(results);
    }



}
