package org.seefly.springmybatis.dao;

import org.apache.ibatis.annotations.Param;
import org.seefly.springmybatis.entity.User;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-12-05 10:47
 */
public interface Demo {

    User selectById(Integer id);

    void insert(User user);

    List<User> selectByCondition(@Param("id") Integer id, @Param("roleId") Integer roleId);
}
