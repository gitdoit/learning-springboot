package org.seefly.springsecurity.config.security;

import org.seefly.springsecurity.custom.entrypoint.CustomAuthenticationEntryPoint;
import org.seefly.springsecurity.custom.handler.LogoutSuccessHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * 资源服务配置
 * <p>
 * {@link ResourceServerConfigurerAdapter} 是spring security下 oauth模块的配置，主要用于定制oauth的规则
 * {@link WebSecurityConfigurerAdapter}则是spring security的配置
 *
 * 这里的tokenServices是用来根据客户端访问资源时携带的Token字符串获取客户端信息，以及完整的令牌信息的
 *
 * {@link EnableResourceServer} 注解引入了一个 {@link ResourceServerConfiguration}
 *
 * @author liujianxin
 */
@Configuration
@EnableResourceServer
public class OauthResourceSecurityConfig  extends ResourceServerConfigurerAdapter {
    /**
     * 用户未登录时访问受限资源的接入点
     */
    @Autowired
    private  CustomAuthenticationEntryPoint EntryPoint;
    @Autowired
    private  LogoutSuccessHandle logoutSuccessHandle;
    @Autowired
    private TokenStore tokenStore;



    /**
     * 资源保护策略
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // oauth安全规则定制
        http.requestMatchers().antMatchers("/users/**")
                .and()
                .authorizeRequests()
                .antMatchers("/users/**").authenticated();
        //匿名用户访问无权限资源处理，登出处理
        http.exceptionHandling().authenticationEntryPoint(EntryPoint).accessDeniedPage("/oauth/403");
    }

    /**
     * 资源服务安全配置
     *
     *  {@link ResourceServerSecurityConfigurer}配置器，它会做三件事
     *  1. 它会产生一个{@link OAuth2AuthenticationProcessingFilter}
     *       该Filter使用固定的AuthenticationManager即OAuth2AuthenticationManager，它并没有将OAuth2AuthenticationManager添加到spring的容器中
     *       不然可能会影响spring security的普通认证流程（非oauth2请求），只有被OAuth2AuthenticationProcessingFilter拦截到的oauth2相关请求才被特殊的身份认证器处理。
     *  2. 配置一个{@link TokenExtractor}
     *  3. 相关的异常处理
     *
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("users-info");
        // ResourceServerTokenServices 定义token的获取、加载，它跟认证服务的tokenService不是同一个东西
        // 在OAuth2AuthenticationManager的认证方法内调用该tokenService
        resources.tokenServices(tokenServices());
    }


    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        return defaultTokenServices;
    }

    /**
     * token存储策略
     */
    /*@Bean(name = "resourceTokenStore")
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }*/
    /**
     * 不知道
     */
    @Bean(name="resourceTokenConverter")
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }
}
