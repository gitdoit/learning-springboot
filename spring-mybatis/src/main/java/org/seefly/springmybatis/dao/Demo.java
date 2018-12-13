package org.seefly.springmybatis.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.seefly.springmybatis.dto.DemoQueryDto;
import org.seefly.springmybatis.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 演示参数使用
 * @author liujianxin
 * @date 2018-12-05 10:47
 */
public interface Demo {
    /**
     * 单个参数可以不用其他操作即可在xml中使用#{id}形式引用该参数
     */
    User selectById(Integer id);

    /**
     * 两个及以上的参数会被封装成一个map，需要使用注解指定每个参数的引用名称，然后使用 #{你指定的名称} 形式引用
     * 又或者通过源码{@link ParamNameResolver#getNamedParams}可以看到mybatis也会默认给每个参数生成对应的引用名称：#{param1} #{param2} ....
     */
    List<User> selectByCondition(@Param("id") Integer id, @Param("roleId") Integer roleId);

    /**
     * 使用dto作为接口参数的时候可以直接在xml中使用 #{dto的成员属性名称}
     * 建议在参数为3个或三个以上的时候使用dto封装
     * 源码:{@link DefaultParameterHandler#setParameters}
     */
    List<User> selectByDto(DemoQueryDto demoQueryDto);

    /**
     * 演示使用Map作为接口参数，和上面的使用dto作为接口参数使用方法一样
     * 它们俩取值的时候都会用{@link MetaObject}类来协助
     * @param params
     * @return
     */
    List<User> selectByMap(Map<String,Integer> params);

    List<User> selectByIds(List<Integer> ids);

    void insert(User user);

}
