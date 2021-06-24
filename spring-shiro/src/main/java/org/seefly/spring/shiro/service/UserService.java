package org.seefly.spring.shiro.service;

import org.seefly.spring.shiro.model.Permissions;
import org.seefly.spring.shiro.model.Role;
import org.seefly.spring.shiro.model.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liujianxin
 * @date 2021/6/8 17:56
 */
@Service
public class UserService {
    private static final Permissions P_QUERY = new Permissions("1", "query");
    private static final Permissions P_ADD = new Permissions("1", "add");
    private static  Role DEFAULT_ROLE = null;
    static {
        Set<Permissions> ps = new HashSet<>();
        ps.add(P_ADD);
        ps.add(P_QUERY);
        DEFAULT_ROLE = new Role("1", "admin",ps);
    }
    
    
    public UserService() {
    
    
    }
    
    public User findUserByUserName(String userName) {
        if (!StringUtils.hasLength(userName)) {
            return null;
        }
        User user = new User();
        Set<Role> r = new HashSet<>();
        r.add(DEFAULT_ROLE);
        if ("a".equals(userName)) {
            user.setUserName("a");
            user.setPassword("123");
            user.setRoles(r);
            return user;
        } else if ("b".equals(userName)) {
            user.setUserName("b");
            user.setPassword("123");
            user.setRoles(r);
            return user;
        }
        return null;
    }
    
    
}
