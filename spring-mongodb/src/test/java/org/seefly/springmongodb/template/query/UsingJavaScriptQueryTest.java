package org.seefly.springmongodb.template.query;

import org.junit.jupiter.api.Test;
import org.seefly.springmongodb.BaseWithoutSpringTest;
import org.seefly.springmongodb.entity.Person;
import org.springframework.data.mongodb.core.aggregation.ScriptOperators;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 *
 * 使用Js脚本会降低查询性能
 * 使用之前最好知道自己在做什么
 * @author liujianxin
 * @date 2021/7/21 11:23
 **/
public class UsingJavaScriptQueryTest extends BaseWithoutSpringTest {

    /**
     * 利用where指令【使用js函数】
     * 返回名字长度大于10的，并且年龄大于30的
     *
     *  { "$where" : "function(){return this.name.length > 10}", "age" : { "$gt" : 30}} fields: Document{{}}
     */
    @Test
    void testWhere(){
        List<Person> list = template.find(query(where("$where").is("function(){return this.name.length > 10}").and("age").gt(30)), Person.class);
        System.out.println(list);
    }

    /**
     * db.person.find( {$expr: { $function: {
     *       body: function(name) { return name.length > 9; },
     *       args: [ "$name" ],
     *       lang: "js"
     * } } } )
     */
    @Test
    void testFunction(){
        List<Person> list = template.find(query(where("$expr").is(ScriptOperators.Function.function("function(name) {return name.length>10;}").args("$name").lang("js"))), Person.class);
        System.out.println(list);
    }
}
