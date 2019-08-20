package org.seefly.springaop.service.impl;

import org.seefly.springaop.domain.User;
import org.seefly.springaop.service.UserService;

/**
 *
 * 演示AOP拦截
 * @author liujianxin
 * @date 2019/8/20 17:29
 */
public class UserServiceImpl implements UserService {
    @Override
    public User createUser(String name, Integer age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);
        return user;
    }

    @Override
    public User queryUser(String name) {
        return null;
    }
}
