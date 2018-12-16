package org.seefly.springsecurity.config;

import org.seefly.springsecurity.handle.CustomAuthenticationEntryPoint;
import org.seefly.springsecurity.handle.LogoutSuccessHandle;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * {@link ResourceServerConfigurerAdapter} 是spring security下 oauth模块的配置，主要用于定制oauth的规则
 * {@link WebSecurityConfigurerAdapter}则是spring security的配置
 *
 * 默认情况下
 * @author liujianxin
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final CustomAuthenticationEntryPoint EntryPoint;
    private final LogoutSuccessHandle logoutSuccessHandle;

    public ResourceServerConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint,LogoutSuccessHandle logoutSuccessHandle){
        EntryPoint = customAuthenticationEntryPoint;
        this.logoutSuccessHandle = logoutSuccessHandle;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("users-info");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // oauth安全规则定制
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .requestMatchers()
            .antMatchers("/users/**")
            .and().authorizeRequests()
            .antMatchers("/users/**")
            .authenticated();
        //匿名用户访问无权限资源处理，登出处理
        http
            .exceptionHandling().authenticationEntryPoint(EntryPoint)
            .and().logout().logoutUrl("/oauth/logout").logoutSuccessHandler(logoutSuccessHandle);
    }

}
