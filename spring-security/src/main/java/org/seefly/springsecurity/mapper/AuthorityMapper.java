package org.seefly.springsecurity.mapper;

import org.apache.ibatis.annotations.Select;
import org.seefly.springsecurity.entity.Authority;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-12-13 20:50
 */
public interface AuthorityMapper {

    @Select("SELECT a.name FROM user_authority ua , authority a  WHERE ua.username = #{userName} AND ua.authority = a.name")
    List<Authority> selectAuthoritysByUserName(String userName);
}
