package org.seefly.springsecurity.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-12-13 20:50
 */
public interface AuthorityMapper {

    @Select("select a.authority as authority  from user_authority as ua left join authority as a on as.authority = a.authority where as.username = #{userName}")
    List<AuthorityMapper> selectAuthoritysByUserName(String userName);
}
