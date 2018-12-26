package org.seefly.springsecurity.custom.handler;

import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 自定义登陆失败的处理逻辑
 * 触发流程：
 *  登陆请求被{@link UsernamePasswordAuthenticationFilter}处被拦截到之后，进行认证。
 *  如果认证失败(密码错误，无该用户)则抛出异常{@link AuthenticationException}
 *  会在父类{@link AbstractAuthenticationProcessingFilter#doFilter}处被捕获，然后调用失败处理逻辑
 *  默认会调用{@link SimpleUrlAuthenticationFailureHandler}
 *
 *  自定义逻辑
 *  1.可以前后端交互，前端异步提交post，登陆失败后像以前一样返回json信息在页面显示
 *
 * @author liujianxin
 * @date 2018-12-13 23:39
 */
public class LoginFailureHandler  implements AuthenticationFailureHandler {



    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 无该用户
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        if(exception instanceof UsernameNotFoundException){
            response.getWriter().println("{\"code\":\"400\",\"msg\":\"无该用户!\"}");
        }
        // 密码错误
        else if(exception instanceof BadCredentialsException){
            response.getWriter().println("{\"code\":\"400\",\"msg\":\"密码错误!\"}");
        }
        response.getWriter().println("{\"code\":\"500\",\"msg\":\"系统异常!\"}");
    }
}
