package org.seefly.aop.basic.evolution.service;

import org.seefly.aop.basic.model.User;

/**
 * @author liujianxin
 * @date 2019/8/20 17:26
 */
public interface UserService {
    
    User createUser(String name, Integer age);
    
    User queryUser(String name);
    
}
