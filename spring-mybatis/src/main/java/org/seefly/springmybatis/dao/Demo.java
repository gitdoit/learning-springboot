package org.seefly.springmybatis.dao;

import org.seefly.springmybatis.entity.User;

/**
 * @author liujianxin
 * @date 2018-12-05 10:47
 */
public interface Demo {

    User selectById(Integer id);

    void insert(User user);
}
