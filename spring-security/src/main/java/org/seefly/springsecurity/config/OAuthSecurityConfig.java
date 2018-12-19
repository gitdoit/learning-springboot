package org.seefly.springsecurity.config;

import org.seefly.springsecurity.handle.CustomAuthenticationEntryPoint;
import org.seefly.springsecurity.handle.LogoutSuccessHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author liujianxin
 * @date 2018-12-19 11:15
 */
//@Configuration
public class OAuthSecurityConfig {



    /**
     * 认证服务配置
     */
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
        private final AuthenticationManager authenticationManager;
        // 注意--> https://stackoverflow.com/questions/31798631/stackoverflowerror-in-spring-oauth2-with-custom-clientdetailsservice
        // 这个bean在声明的时候需要加一个@Primary 不然你就栈溢出
        private final ClientDetailsService clientDetailsService;

        /**
         * 构造方法
         * @param authenticationManager  认证管理
         * @param clientDetailsService 自定义客户端获取方式
         */
        @Autowired
        public AuthorizationServerConfig(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager, ClientDetailsService clientDetailsService) {
            this.authenticationManager = authenticationManager;
            this.clientDetailsService = clientDetailsService;
        }

        /**
         * 自定义token存储方式
         */
        @Bean
        public TokenStore tokenStore(){
            InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore();
            return  inMemoryTokenStore;
        }


        /**
         * 配置客户端校验方式
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            // 自定义客户端校验
            clients.withClientDetails(clientDetailsService);

        }

        /**
         * /oauth/authorize：授权端点
         * /oauth/token：令牌端点
         * /oauth/confirm_access：用户确认授权提交端点
         * /oauth/error：授权服务错误信息端点
         * /oauth/check_token：用于资源服务访问的令牌解析端点
         * /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话
         * @param endpoints
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager)
                    .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenStore(tokenStore());
            //endpoints.pathMapping("/oauth/confirm_access","/oauth/my_confirm_access");
        }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                //code授权添加
                .realm("oauth2-resources")
                //允许所有资源服务器访问公钥端点（/oauth/token_key）
                .tokenKeyAccess("permitAll()")
                //只允许验证用户访问令牌解析端点（/oauth/check_token）
                .checkTokenAccess("isAuthenticated()")
                // 允许客户端发送表单来进行权限认证来获取令牌
                .allowFormAuthenticationForClients();
    }
    }


    /**
     * 资源服务配置
     *
     * {@link ResourceServerConfigurerAdapter} 是spring security下 oauth模块的配置，主要用于定制oauth的规则
     * {@link WebSecurityConfigurerAdapter}则是spring security的配置
     *
     * @author liujianxin
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfig extends ResourceServerConfigurerAdapter{
        /**用户未登录时访问受限资源的接入点*/
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
            http.requestMatchers().antMatchers("/users/**")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/users/**").authenticated();

            //匿名用户访问无权限资源处理，登出处理
            http.exceptionHandling().authenticationEntryPoint(EntryPoint).accessDeniedPage("/oauth/403");
        }
    }
}
