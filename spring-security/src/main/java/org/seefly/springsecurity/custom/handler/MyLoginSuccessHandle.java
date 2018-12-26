package org.seefly.springsecurity.custom.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * 自定义登陆成功处理
 * 在{@link AbstractAuthenticationProcessingFilter#doFilter}登陆成功后被调用
 * 若不配置，则默认使用{@link SavedRequestAwareAuthenticationSuccessHandler}来处理登陆成功后的逻辑。
 * 它的逻辑是在登陆成功后将用户重定向到源路径，若无原路径，则使用它的父类{@link SimpleUrlAuthenticationSuccessHandler}的处理逻辑
 * 即重定向到你配置的路径，默认为首页。
 * 关于源路径：若是用户直接访问登陆页面则无原路径，若是访问受限资源被重定向到登陆页的，则源路经为之前想要访问的那个受限资源路径。
 * <p>
 * <p>
 * 我们可以自定义此类，实现如下逻辑
 * 1.若有源访问路径，则跳转
 * 2.若无，则根据权限不同，【转发】到不同页面。
 *
 * @author liujianxin
 * @date 2018-12-25 13:51
 */
public class MyLoginSuccessHandle extends SimpleUrlAuthenticationSuccessHandler {
    /**
     * 源请求缓存，在ExceptionTranslationFilter捕获到异常时放入原请求
     */
    private HttpSessionRequestCache cache = new HttpSessionRequestCache();
    private SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
    private SimpleGrantedAuthority user = new SimpleGrantedAuthority("ROLE_USER");

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        // 判断是否为认证异常重定向
        String targetUrl = "/";
        SavedRequest savedRequest = cache.getRequest(request, response);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 直接登陆的
        if (savedRequest == null && !CollectionUtils.isEmpty(authorities)) {
            //根据认证信息判断身份跳转不同路径
            if (authorities.contains(admin)) {
                targetUrl = "/admin";
            } else if (authorities.contains(user)) {
                targetUrl = "/user";
            }
        } else if (savedRequest != null) {
            targetUrl = savedRequest.getRedirectUrl();
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
