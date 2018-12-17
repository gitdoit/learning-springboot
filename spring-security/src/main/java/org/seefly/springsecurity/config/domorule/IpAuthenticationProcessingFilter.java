package org.seefly.springsecurity.config.domorule;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liujianxin
 * @date 2018-12-17 16:27
 */
public class IpAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public IpAuthenticationProcessingFilter() {
        // 对指定路径使用ip校验
        super("/ipVerify");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String ip = request.getRemoteHost();
        // 交给认证器认证
        return getAuthenticationManager().authenticate(new IpAuthenticationToken(ip));
    }
}
