package org.seefly.springmybaits;

import org.junit.runner.RunWith;
import org.seefly.springmybaits.mapper.ArUserMapper;
import org.seefly.springmybaits.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liujianxin
 * @date 2018-12-01 21:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected ArUserMapper arUserMapper;
}
