package org.seefly.springmongodb.projections;

import org.seefly.springmongodb.entity.Person;
import org.seefly.springmongodb.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Value;

/**
 * 用于投影，大白话就是只想从数据库里查询指定的字段，避免数据量太大OOM
 * 用该接口当做返回值，框架会自动推断
 * @see PersonRepository#findTop3ByName(java.lang.String)
 *
 * 这里定义的接口必须和 {@link Person}中的字段的getter相同
 * @author liujianxin
 * @date 2021/7/29 17:03
 **/
public interface NameHobby {
    
    String getName();
    
    /**
     * 将原来List<String> hobbies，转成字符串
     * 这种方式不能进行查询优化， 也就是说会查询所有字段
     *
     * 另外，还能用@bean.FoomMethod(target)的方式调用容器中bean的方法
     */
    @Value("#{T(java.lang.String).join(',',target.hobbies)}")
    String getHobbies();

}
