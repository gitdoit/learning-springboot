package org.seefly.springsecurity.config.domorule;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义认证管方式,使用地址校验
 * @author liujianxin
 * @date 2018-12-17 16:31
 */
public class IpAuthenticationProvider implements AuthenticationProvider {
    final static Map<String, SimpleGrantedAuthority> IP_USER = new ConcurrentHashMap<>();

    static {
       // IP_USER.put("127.0.0.1",new SimpleGrantedAuthority("ADMIN"));
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        IpAuthenticationToken token = (IpAuthenticationToken)authentication;
        String ip = token.getIp();
        SimpleGrantedAuthority authority = IP_USER.get(ip);
        if(authority == null){
            return null;
        }
        return new IpAuthenticationToken( Collections.singletonList(authority),ip);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return IpAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
