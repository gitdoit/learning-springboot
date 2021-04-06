package org.seefly.mybatis.mapper;

import org.seefly.mybatis.entity.User;

import java.util.List;

/**
 * @author liujianxin
 * @date 2021/4/5 21:36
 */
public interface UserMapper {
    
    List<User> listAll();
    
}
