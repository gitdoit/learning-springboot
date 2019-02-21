package org.seefly.springsecurity.custom.entrypoint;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 配置401未认证时的响应信息
 * · 500 internal sever error，表示服务器端在执行请求时发生了错误
 * · 503 handle unavailable，表明服务器暂时处于超负载或正在停机维护，无法处理请求

 * · 400 bad request，请求报文存在语法错误
 * · 401 unauthorized，表示发送的请求需要有通过 HTTP 认证的认证信息
 * · 403 forbidden，表示对请求资源的访问被服务器拒绝
 * · 404 not found，表示在服务器上没有找到请求的资源
 *
 * · 301 moved permanently，永久性重定向，表示资源已被分配了新的 URL
 * · 302 found，临时性重定向，表示资源临时被分配了新的 URL
 * · 303 see other，表示资源存在着另一个 URL，应使用 GET 方法丁香获取资源
 * · 304 not modified，表示服务器允许访问资源，但因发生请求未满足条件的情况
 * · 307 temporary redirect，临时重定向，和302含义相同
 *
 * · 200 OK，表示从客户端发来的请求在服务器端被正确处理
 * · 204 No content，表示请求成功，但响应报文不含实体的主体部分
 * · 206 Partial Content，进行范围请求
 *
 *
 *
 *  默认情况下用户在访问受限资源但没有登陆时会抛出一个{@link AuthenticationException}异常，并在
 *  {@link ExceptionTranslationFilter}处被捕获，然后调用默认的{@link LoginUrlAuthenticationEntryPoint}进入认证端点
 *
 *
 * @author liujianxin
 * @date 2018-12-14 11:29
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "请先登陆，再访问该资源！");
    }
}
