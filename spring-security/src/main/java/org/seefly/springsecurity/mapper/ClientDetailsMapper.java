package org.seefly.springsecurity.mapper;

import org.apache.ibatis.annotations.Select;
import org.seefly.springsecurity.entity.ClientDetails;

/**
 * @author liujianxin
 * @date 2018-12-24 14:00
 */
public interface ClientDetailsMapper {

    @Select("select * from client_details where client_id = #{clientId}")
    ClientDetails findByClientId(String clientId);

}
