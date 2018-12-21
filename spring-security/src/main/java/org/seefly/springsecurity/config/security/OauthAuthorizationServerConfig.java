package org.seefly.springsecurity.config.security;

import org.seefly.springsecurity.custom.CustomTokenEnhancer;
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
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

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
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // 一个组合的token增强链
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenStore(tokenStore())
                // 令牌存储方式
                .tokenStore(tokenStore())
                // jwt方式
                .accessTokenConverter(jwtAccessTokenConverter())
                // 添加token的额外信息
                .tokenEnhancer(tokenEnhancerChain);


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
     * TokenStore实现，只从令牌本身读取数据。它不是真正的存储，因为它永远不会存储存任何东西。
     * getAccessToken（OAuth2Authentication）等方法总是返回null。
     * 但它仍然是一个有用的工具，因为它将访问令牌转换为身份验证和从身份验证转换。
     * 在需要TokenStore的任何地方使用它，但是记住使用与令牌被铸造时使用的相同的JwtAccessTokenConverter实例（或具有相同验证器的实例）
     *
     * 也就是说授权服务器的accessTokenConverter和资源服务器的accessTokenConverter要一样
     */
    @Bean(name="authTokenStore")
    public TokenStore tokenStore() {
        //InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 创建额外的令牌信息
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    /**
     *
     * 配置AuthorizationServer安全认证的相关信息
     * 创建{@link ClientCredentialsTokenEndpointFilter}核心过滤器,用来拦截令牌申请，即 /oauth/token
     *  Doc:
     *      OAuth2令牌端点的过滤器和身份验证终结点。允许客户端使用请求参数进行身份验证（如果包含为安全筛选器），如规范所允许（但不推荐）。
     *      规范建议您允许客户端进行HTTP基本身份验证，而根本不使用此过滤器
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
