package org.seefly.mybatisplus.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.seefly.mybatisplus.model.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    /**
     * 这个方法用来演示怎么样使用@param
     * @param loginName
     * @param loginPwd
     * @return
     */
    List<User> selectOne(@Param("loginName") String loginName, @Param("loginPwd")String loginPwd);
}