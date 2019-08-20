package org.seefly.springaop.service;

import org.seefly.springaop.domain.User;

/**
 * @author liujianxin
 * @date 2019/8/20 17:26
 */
public interface UserService {

   User createUser(String name,Integer age);

   User queryUser(String name);

}
