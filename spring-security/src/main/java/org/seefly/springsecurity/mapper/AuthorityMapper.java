package org.seefly.springsecurity.mapper;

import org.apache.ibatis.annotations.Select;
import org.seefly.springsecurity.entity.Authority;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-12-13 20:50
 */
public interface AuthorityMapper {

    @Select("SELECT a.`name` FROM user u ,realetionship r,authority a WHERE u.username = #{userName}  AND r.type = 'user_auth' AND u.id = r.from_id AND r.to_id = a.id")
    List<Authority> selectAuthoritysByUserName(String userName);
}
