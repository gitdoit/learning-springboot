package org.seefly.springsecurity.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Oauth的授权服务器配置
 *
 * @author liujianxin
 * @date 2018-12-21 09:51
 */
@Configuration
@EnableAuthorizationServer
public class OauthAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final AuthenticationManager authenticationManager;
    /**
     * 注意--> https://stackoverflow.com/questions/31798631/stackoverflowerror-in-spring-oauth2-with-custom-clientdetailsservice
     * 这个bean在声明的时候需要加一个@Primary 不然你就栈溢出
     */
    private final ClientDetailsService clientDetailsService;


    /**
     * @param authenticationManager 认证管理
     * @param clientDetailsService  自定义客户端获取方式
     */
    @Autowired
    public OauthAuthorizationServerConfig(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager, ClientDetailsService clientDetailsService) {
        this.authenticationManager = authenticationManager;
        this.clientDetailsService = clientDetailsService;
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
     * <p>
     * 配置AuthorizationServerEndpointsConfigurer众多相关类，包括配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactor
     *
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenStore(tokenStore())
                // 令牌存储方式
                .tokenStore(tokenStore())
                // 令牌
                .accessTokenConverter(jwtAccessTokenConverter());
        //endpoints.pathMapping("/oauth/confirm_access","/oauth/my_confirm_access");
    }

    /**
     * 使用同一个密钥来编码 JWT 中的  OAuth2 令牌
     */
    @Bean(name = "authTokenConverter")
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }
    /**
     * 自定义token存储方式
     */
    @Bean(name="authTokenStore")
    public TokenStore tokenStore() {
        //InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 配置AuthorizationServer安全认证的相关信息
     * 创建{@link ClientCredentialsTokenEndpointFilter}核心过滤器,用来拦截令牌申请，即 /oauth/token
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
