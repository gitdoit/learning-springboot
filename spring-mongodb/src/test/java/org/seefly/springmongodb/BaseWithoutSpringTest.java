package org.seefly.springmongodb;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.utils.MongoClientUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 * usage of MongoTemplate
 *      https://github.com/spring-projects/spring-data-mongodb/tree/ef29e69a87022db0ca0e475dc4b276dccab0597d/spring-data-mongodb/src/test/java/org/springframework/data/mongodb/core
 * MySQL VS Mongo
 *      https://stackoverflow.com/questions/9702643/mysql-vs-mongodb-1000-reads
 *     性能并不是使用Mongo的考量依据，很多情况下他和MySQL区别并不大。我也搞不懂
 * @author liujianxin
 * @date 2021/7/20 15:25
 **/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseWithoutSpringTest {
    protected MongoTemplate template;

    @BeforeAll
    void before() {
        template = MongoClientUtil.create("test");
    }
}
