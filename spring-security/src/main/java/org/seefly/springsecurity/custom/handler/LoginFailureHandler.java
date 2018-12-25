package org.seefly.springsecurity.custom.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
public class LoginFailureHandler  {
}
