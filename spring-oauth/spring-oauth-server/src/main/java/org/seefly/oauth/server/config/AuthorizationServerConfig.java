package org.seefly.oauth.server.config;

import org.seefly.oauth.server.security.SeeflyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * 认证服务器配置
 * @author liujianxin
 * @date 2020/5/11 21:46
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SeeflyUserService userService;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**
         * 这里就相当于我是腾讯，在这里配置众多其他第三方应用的信息
         * 一般都是向腾讯申请接入QQ，通过后通讯在这里配置第三方应用的信息
         *
         * 用户使用QQ账号在第三方平台登录，第三方平台将用户重定向
         * 到QQ的登录页面，携带参数如下
         * http://QQ安全登录中心?response_type=code&client_id=admin&redirect_uri=http://www.baidu1.com&scope=all&state=normal
         * response_type：授权类型，(code就是授权码模式)
         * client_id：第三方id，也就是从哪个第三方平台来的
         * redirect_uri：登录成功后需要重定向到哪里
         *
         * client_id和redirect_url都是要在这里事先配置好的
         * 在请求地址中瞎几巴填不会通过
         */
        clients.inMemory()
                //配置client_id，也就是相当于第三方应用的账号
                .withClient("admin")
                //配置client_secret 第三方应用的密码
                .secret(passwordEncoder.encode("admin"))
                //配置访问token的有效期
                .accessTokenValiditySeconds(3600)
                //配置刷新token的有效期
                .refreshTokenValiditySeconds(864000)
                //配置redirect_uri，用于授权成功后跳转；
                .redirectUris("http://www.baidu.com")
                //配置申请的权限范围
                .scopes("all")
                //配置grant_type，表示授权类型
                .authorizedGrantTypes("authorization_code","password");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService);
    }


}
