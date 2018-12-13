package org.seefly.springsecurity.QueryTest;

import org.junit.Test;
import org.seefly.springsecurity.BaseTest;
import org.seefly.springsecurity.entity.Authority;
import org.seefly.springsecurity.entity.User;
import org.seefly.springsecurity.mapper.AuthorityMapper;
import org.seefly.springsecurity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-12-13 20:40
 */
public class UserTest extends BaseTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthorityMapper authorityMapper;

    @Test
    public void testSelectByUserName(){
        User abc = userMapper.selectByUserName("abc");
        System.out.println(abc);
    }

    @Test
    public void testSelectAuthority(){
        List<Authority> admin = authorityMapper.selectAuthoritysByUserName("admin");
        admin.forEach(System.out::print);
    }

}
