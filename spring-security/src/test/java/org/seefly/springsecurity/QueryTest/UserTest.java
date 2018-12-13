package org.seefly.springsecurity.QueryTest;

import org.junit.Test;
import org.seefly.springsecurity.BaseTest;
import org.seefly.springsecurity.entity.User;
import org.seefly.springsecurity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liujianxin
 * @date 2018-12-13 20:40
 */
public class UserTest extends BaseTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectByUserName(){
        User abc = userMapper.selectByUserName("abc");
        System.out.println(abc);
    }
}
