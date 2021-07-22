package org.seefly.springmongodb.query.aggregation;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.springframework.data.mongodb.core.aggregation.*;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 管道操作就像是java8中的stream概念类似
 * 数据在管道中流过，每个阶段都会有自己的处理方式，然后将处理结果传到下一个阶段
 * @author liujianxin
 * @date 2021/7/16 11:19
 **/
public class AggregationQuery extends BaseWithoutSpringTest {

    /**
     * 【查询返回指定字段】
     * select name,age,substr(name,0,2) as sub from person where age > 20
     *  [{ "$match" : { "age" : { "$gt" : 20}}}, { "$project" : { "name" : 1, "age" : 1, "_id" : 0, "sub" : { "$substr" : ["$name", 0, 2]}}}]
     *
     *  -> {name=Running_Laptop, age=42, sub=Ru, doubleAge=84}
     */
    @Test
    void projectionTest(){
        AggregationResults<Document> aggregate = template.aggregate(
                newAggregation(
                        new MatchOperation(where("age").gt(20)),
                        new ProjectionOperation(Fields.fields("name", "age")).andExclude("_id")
                        .and("name").substring(0,2).as("sub")
                        .andExpression("age * 2").as("doubleAge")
                ),
                "person",
                Document.class
        );
        List<Document> results = aggregate.getMappedResults();
        System.out.println(results);
    }







    /**
     * 【$replaceRoot】的意思就是提升到根
     *      比如a嵌套的包含b对象，将b对象的几个字段提升到a中
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






}
