package org.seefly.springsecurity.mapper;

import org.apache.ibatis.annotations.Select;
import org.seefly.springsecurity.entity.User;

/**
 * @author liujianxin
 * @date 2018-12-13 20:35
 */
public interface UserMapper {

    @Select("select * from user where username = #{userName}")
    User selectByUserName(String userName);
}
