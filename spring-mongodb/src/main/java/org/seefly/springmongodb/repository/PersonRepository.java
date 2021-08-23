package org.seefly.springmongodb.repository;

import org.seefly.springmongodb.context.SecurityEvaluationContext;
import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.entity.aggregate.PersonAggregate;
import org.seefly.springmongodb.extend.TenantQuery;
import org.seefly.springmongodb.projections.DynamicProjections;
import org.seefly.springmongodb.projections.NameHobby;
import org.seefly.springmongodb.wrapper.Persons;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * <p>
 * 注意,对与条多条结果的返回值类型
 *  1、List 最常见的，所有结果都从数据库中取出来的时候才会返回
 *  2、Streamable Spring data包装的，用来方便的处理可迭代的集合
 *  3、Stream 流式数据，这代表着当第一条数据被返回的时候就已经开始处理了，而不必等到所有数据都从数据库返回
 * </p>
 * 本类所有的示例都来<a href="https://docs.spring.io/spring-data/mongodb/docs/3.2.3/reference/html/#reference">官方文档</a>
 *
 * @author liujianxin
 * @date 2021/7/28 16:44
 **/
public interface PersonRepository extends MongoRepository<Person,String>,CustomizedRepository<Person>{
    
    
    List<Person> findPersonByAgeIn(List<Integer> ages);
    
    List<Person> findPersonByNameOrAge(String name,Integer age);
    
    List<Person> findPersonByNameLikeAndHeightBetween(String name,Integer low,Integer hei);
    
    List<Person> findPersonByNameLike(String start, Pageable pageable);
    
    List<Person> findTop5ByAgeGreaterThan(Integer age, Sort sort);
    
    /**
     * 返回流式数据，常用来处理数据量较大的场景
     * <p>
     * 	A Stream potentially wraps underlying data store-specific resources and must, therefore, be closed after usage.
     * 	You can either manually close the Stream by using the close() method or by using a Java 7 try-with-resources block
     * 	<p/>
     * 	<a href="https://docs.spring.io/spring-data/mongodb/docs/3.2.3/reference/html/#repositories.query-methods.query-property-expressions">Spring Data Doc<a/>
     */
    Stream<Person> findPersonByNameContaining(String name);
    
    /**
     * 和Stream不一样，Streamable仅仅是方便处理可迭代集合
     */
    Streamable<Person> findByAgeGreaterThan(Integer age);
    
    
    /**
     * 返回一个包装类型，这样可以包含一些业务逻辑，比较好用
     * @see Persons
     */
    Persons findPersonByAgeNotIn(List<Integer> ages);
    
    /**
     * 返回一个异步执行的结果
     * 当方法被调用后会立即返回，任务将交给Spring提供的TaskExecutor执行
     * The method returns immediately upon invocation while the actual query occurs in a task that has been submitted to a Spring TaskExecutor
     */
    @Async
    Future<Person> findTopByName(String name);
    
    /**
     * 使用查询语句
     *  语法和些原生mongodb查询语句一样,唯一不同的是参数占位符
     *  ?0 表示方法签名中的第一个参数，?1表示第二个，以此类推
     */
    @Query(value = "{'name':?0,'age':?1}")
    Stream<Person> findByTheAgeAndNameIs(String name,Integer age);
    
    
    /**
     * 语句还支持SpEl表达式
     *  在SpEl模板中使用[0]访问第一个参数,使用#paramName访问指定参数
     *
     *  { 'age':{ $gt: ?#{#age} } } 等同于 { 'age':{ $gt: ?#{[0]} } }
     *
     *  了解SpEl的都知道在SpEl模板中，通过#符号来引用上下文中的参数
     */
    @Query(value = "{ 'age':{ $gt: ?#{#age} } }")
    List<Person> findBySpEl(Integer age);
    
    
    /**
     * userName是什么？
     * @see SecurityEvaluationContext
     * 给SpEl表达式的执行环境扩展一个执行上下文，这样框架在解释SpEl语句的时候能够获取到当前用户的信息以及其他东西等等
     * 比较好的用处是根据当前用户登录信息获取指定的数据
     */
    @Query(value = "{'name': ?#{userName}}")
    List<Person> findByContextUser();
    
    /**
     * 通过SpEl引用方法，Spring Security的 hasRole('xxx') 功能不就是靠这个实现的么
     * @see SecurityEvaluationContext.SecurityExpressionRoot#randomAge()
     * @see SecurityEvaluationContext#staticName()
     */
    @Query(value = "{$or: [{'age':?#{randomAge()}},{'name':?#{staticName()}}]}")
    List<Person> findBySpElAndUseFunction();
    
    /**
     * 只返回指定字段，最简单的方法就是定义一个接口
     * 然后接口里面只定义一些字段同名getter
     * 然后框架就会知道你想要查询哪些字段,然后利用代理
     * 在内存中动态生成对象,并只有指定字段
     *
     */
    List<NameHobby> findTop3ByName(String name);
    
    /**
     * 只查询指定字段
     * 这个比上面的简单多了
     */
    @Query(fields = "{'age':1,'_id':0}")
    List<Person> findTop2ByName(String name);
    
    /**
     * 动态投影
     * t传啥，就查啥字段
     * @see DynamicProjections
     */
    <T> List<T> findTop6ByName(String name,Class<T> t);
    
    
    /***********************************Aggregate********************************************************/
    
    /**
     *
     * 先按年龄过滤, 再按照年龄分组，同一组的姓名组成一个数组
     * usage see {@link Aggregation#pipeline()}
     */
    @TenantQuery
    @Aggregation(pipeline ={"{ $group:{_id:$age,names:{$addToSet:$name}} }"})
    List<PersonAggregate> groupByAgeAndGreaterThen(Integer age);
    
    /**
     * 先按年龄过滤，在执行一个自定义脚本，最后分组求和；
     * 注意，聚合管道查询只能使用$expr包一下$function来执行脚本，不能直接$where执行，那个是用来普通查询的
     *
     */
    @TenantQuery
    @Meta(allowDiskUse = true)
    @Aggregation(pipeline = {"{$match:{age:{$gte:?0},$expr:{$function:{body: 'function(name) { return name.length>10; }', args: ['$name'],lang: 'js' }  }  }}}","{$group:{_id:null,total:{$sum:$age}}}"})
    Long sumAgeThatGreaterThen(Integer age);



    /***********************************tenant多租户********************************************************/

    @TenantQuery
    List<Person> findByHeightBetween(Integer low,Integer height);

}
