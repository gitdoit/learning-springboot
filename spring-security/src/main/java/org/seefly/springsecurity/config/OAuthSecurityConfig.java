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
import org.springframework.security.oauth2.config.annotation.web.configuration.*;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author liujianxin
 * @date 2018-12-19 11:15
 */
public class OAuthSecurityConfig {


    /**
     * 认证服务配置
     * 这里的tokenServices是用来创建、刷新、获取Token的
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
         *
         * @param authenticationManager 认证管理
         * @param clientDetailsService  自定义客户端获取方式
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
        public TokenStore tokenStore() {
            InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore();
            return inMemoryTokenStore;
        }


        /**
         * 配置客户端信息
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
         *
         *  配置AuthorizationServerEndpointsConfigurer众多相关类，包括配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactor
         * @param endpoints
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager)
                    .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenStore(tokenStore());
            //endpoints.pathMapping("/oauth/confirm_access","/oauth/my_confirm_access");
        }

        /**
         *  配置AuthorizationServer安全认证的相关信息
         *  创建{@link ClientCredentialsTokenEndpointFilter}核心过滤器,用来拦截令牌申请，即 /oauth/token
         */
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
     * <p>
     * {@link ResourceServerConfigurerAdapter} 是spring security下 oauth模块的配置，主要用于定制oauth的规则
     * {@link WebSecurityConfigurerAdapter}则是spring security的配置
     *
     * 这里的tokenServices是用来根据客户端访问资源时携带的Token字符串获取客户端信息，以及完整的令牌信息的
     *
     *  {@link EnableResourceServer} 注解引入了一个 {@link ResourceServerConfiguration}
     *
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfig extends ResourceServerConfigurerAdapter {
        /**
         * 用户未登录时访问受限资源的接入点
         */
        private final CustomAuthenticationEntryPoint EntryPoint;
        private final LogoutSuccessHandle logoutSuccessHandle;

        public ResourceServerConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, LogoutSuccessHandle logoutSuccessHandle) {
            EntryPoint = customAuthenticationEntryPoint;
            this.logoutSuccessHandle = logoutSuccessHandle;
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
