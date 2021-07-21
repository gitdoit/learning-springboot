package org.seefly.springmongodb.aggregation;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author liujianxin
 * @date 2021/7/16 11:19
 **/
public class AggregationQuery extends BaseWithoutSpringTest {

    /**
     * 【$replaceRoot】的意思就是提升到根
     *      比如嵌套的a包含b对象，将b对象的几个字段提升到a中
     *
     * db.getCollection("nestedEntity").aggregate([
     *  {$match: { value: {$exists: true}}},
     *  {$replaceRoot: {newRoot: "$value"}}
     * ])
     */
    @Test
    void replaceRootTest(){
        Aggregation aggregation = Aggregation.newAggregation(
                match(where("value").exists(true)),
                replaceRoot("value")
        );
        AggregationResults<Document> result = template.aggregate(aggregation, "nestedEntity", Document.class);
        System.out.println(result.getUniqueMappedResult());

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
