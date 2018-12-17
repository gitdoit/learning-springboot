package org.seefly.springsecurity.service;

import org.apache.commons.lang3.StringUtils;
import org.seefly.springsecurity.entity.Authority;
import org.seefly.springsecurity.mapper.AuthorityMapper;
import org.seefly.springsecurity.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private UserMapper userMapper;
    private AuthorityMapper authorityMapper;
    public MyUserDetailsServiceImpl(UserMapper userMapper,AuthorityMapper authorityMapper){
        this.userMapper = userMapper;
        this.authorityMapper = authorityMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {return null;}
        org.seefly.springsecurity.entity.User user = userMapper.selectByUserName(username);
        if(Objects.isNull(user)){
            // 接口规范，找不到抛异常。为什么
            throw new UsernameNotFoundException("没有该用户");
        }
        List<Authority> authoritys = authorityMapper.selectAuthoritysByUserName(user.getUsername());
        Collection<GrantedAuthority> authorities =null;
        if(!CollectionUtils.isEmpty(authoritys)){
            authorities = authoritys.stream().map(au ->new SimpleGrantedAuthority(au.getName())).collect(Collectors.toSet());
        }
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
