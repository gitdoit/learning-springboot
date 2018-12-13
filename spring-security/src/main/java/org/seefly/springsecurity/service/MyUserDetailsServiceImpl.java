package org.seefly.springsecurity.service;

import org.seefly.springsecurity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

/**
 * user.isAccountNonExpired() 账号没有过期 true
 * user.isAccountNonLocked() 账号没锁定 true
 * user.isCredentialsNonExpired() 凭据没有过期 true  过期的凭据阻止身份验证
 * user.isEnabled() 账户已经启用 true  没启用的账户阻止身份验证
 *
 * @author liujianxin
 * @date 2018-12-12 13:57
 */
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        if ("admin".equals(username)) {
            Collection<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("admin"));
            return new User("admin", "admin", authorities);
        }
        return null;
    }
}
