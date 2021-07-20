package org.seefly.springmongodb;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.seefly.springmongodb.utils.MongoClientUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
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
