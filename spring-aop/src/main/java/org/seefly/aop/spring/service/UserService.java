package org.seefly.aop.spring.service;

import org.seefly.aop.spring.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2021/7/28 10:39
 **/
@Service
public class UserService {
    private Map<String,User> userMap = new HashMap<String,User>();

    @PostConstruct
    public void post(){
        userMap.put("michael",new User("michael",12));
        userMap.put("jean",new User("jean",121));
    }

    public User findByName(String name){
        return userMap.get(name);
    }

    public void createUser(User user){
        userMap.put(user.getName(),user);
    }
}
