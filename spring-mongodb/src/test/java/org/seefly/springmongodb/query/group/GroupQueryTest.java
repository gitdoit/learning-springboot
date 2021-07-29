package org.seefly.springmongodb.query.group;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * 至今没有找到在MongoTemplate中
 * 按照某个字段经过计算之后结果分组的实现方式，例如按照姓名长度进行分组
 *
 * @author liujianxin
 * @date 2021/7/22 11:39
 **/
public class GroupQueryTest extends BaseWithoutSpringTest {

    /**
     * 统计表中所有的年龄之和
     * select sum(age) from person
     * [{ "$group" : { "_id" : null, "age" : { "$sum" : "$age"}}}, { "$project" : { "age" : 1}}]
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

    /**
     * 按照年龄分组，年龄相同的组成数组显示
     *
     *  [{ "$group" : { "_id" : "$age", "names" : { "$push" : "$name"}}}]
     *
     *   --->{_id=23, names=[Shoes_Kitty, Video_games_Cone]}
     *
     */
    @Test
    void testGroupByName(){
        AggregationResults<Document> aggregate = template.aggregate(
                //                                               $$ROOT即代表这条数据本身
                newAggregation(group("age").push("name").as("names")), "person", Document.class);
        List<Document> mappedResults = aggregate.getMappedResults();
        System.out.println(mappedResults);
    }




    /**
     *
     * 按名字长度分组，求组内平均年龄和数量，没有找到用MongoTemplate的实现方式
     * db.person.aggregate(
     *    [
     *       {
     *         $group : {
     *            _id : { $function: {body:function(name){return name.length},args:["$name"],lang:"js" } },
     *            averageQuantity: { $avg: "$age" },
     *            count: { $sum: 1 }
     *         }
     *       }
     *    ]
     * )
     *
     * db.person.aggregate(
     *    [
     *       {
     *         $group : {
     *            _id : {$strLenBytes: "$name" },
     *            averageQuantity: { $avg: "$age" },
     *            count: { $sum: 1 }
     *         }
     *       }
     *    ]
     * )
     *
     *
     */
    @Test
    void testGroupByNameLength(){

        AggregationResults<Document> aggregate = template.aggregate(
                newAggregation(
                        group(Fields.fields("name"))
                        .first("name").as("name")
                ),
                "person",
                Document.class
        );
        System.out.println(aggregate.getMappedResults());
    }
}
