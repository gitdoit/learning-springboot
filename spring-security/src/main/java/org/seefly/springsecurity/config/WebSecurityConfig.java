package org.seefly.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 向容器中添加任意一个实现了{@link WebSecurityConfigurer}的接口的bean
 * 并使用{@link EnableWebSecurity}注解开启Spring Security
 * 如果应用是一个 spirng mvc应用 则可以使用{@link EnableWebMvcSecurity},此时处理方法可以使用{@link AuthenticationPrincipal}注解获得认证用户的principal
 *
 * 关于{@link EnableWebSecurity}
 * 这个注解中引入了{@link WebSecurityConfiguration}，而{@link WebSecurityConfiguration#setFilterChainProxySecurityConfigurer}方法上
 * 使用了自动注入注解，并配合{@value}注入所有实现了{@link WebSecurityConfigurer}接口的配置类，然后通过他们的Order级别顺序对它们的配置进行应用
 * 就是调用这个{@link AbstractConfiguredSecurityBuilder#apply}
 *
 *
 * 本类为WebSecurityConfigurer接口的一个实现类，它的order=100，而 ResourceServerConfig 是oauth的一个配置类也实现了同样的接口，它的order=3；
 * 疑问就是不是order越小优先级越高吗？debug看过去为什么先使用了ResourceServerConfig的配置，然后再使用了这个类的配置。这样优先级低的这个类的配置
 * 不就覆盖了优先级高的了吗？网上看了一下，说就是这样的，oauth的配置如果相同就是会被覆盖
 *
 *
 *
 * 关于requestMatchers的使用说明
 * <a href="https://stackoverflow.com/questions/38527723/what-is-the-reason-to-use-requestmatchers-antmatchers-without-a-verb-in-spri"/>
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * 对登陆用户进行校验管理，设置对登陆用户的身份校验方式
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 基于jdbc的验证，并可以重写查询用户及权限的语句,设置密码加密，数据库中存储加密后的密码，前端密码传入后加密进行比较
        // auth.jdbcAuthentication().dataSource().usersByUsernameQuery().authoritiesByUsernameQuery().passwordEncoder()

        // 基于内存认证
        // auth.inMemoryAuthentication().withUser("admin").password("admin").authorities("ROLE_USER");
        // 等同于下面，因为 roles()方法会自动添加前缀ROLE_
        // auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");

        // 这种方式可以提供一个service，实现一个根据用户名查询用户信息的接口，具体的如何获取用户信息逻辑自己实现
        auth.userDetailsService(userDetailsService);
    }


    /**
     * 配置安全规则需要将最为具体的请求路径放在前面，不具体的放在后面
     * 因为规则会顺序验证，前一条通不过后一条不会再校验。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 默认启用防跨域攻击，这里可以禁用
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/private/**").authenticated();

    }



    /**
     * 路径安全规则示例
     */
    private void configure1(HttpSecurity http) throws Exception {
        //1. 对 /private/** 路径下的请求，必须已经登陆,不关注其他请求
        http.authorizeRequests().antMatchers("/private/**").authenticated();

        //2. 对 /abc/** 路径下的请求，必须有ROLE_ADMIN权限。除此之外的其他请求(/**) 都拒绝
        http.authorizeRequests().antMatchers("/abc/**").hasRole("ADMIN").anyRequest().denyAll();

        //3. 对 /abc/** 路径下的请求，允许匿名访问。对 /private/** 路径下的请求 必须具有 CHECK DELETE 中的至少一种权限。
        http.authorizeRequests().antMatchers("/abc/**").anonymous().antMatchers("/private/**").hasAnyAuthority("CHECK","DELETE");

        //4. SPeL表达式，允许指定IP访问，并且必须具有指定角色。
        http.authorizeRequests().antMatchers("/abc/**").access("hasRole('ROLE_USER') and hasIpAddress('192.168.6.*')");

        //5. 【错误用法】 第一个规则范围大于第二个，第一个规则不管校验成功还是失败，都不会执行第二个校验规则
        http.authorizeRequests().antMatchers("/**").hasRole("USER").antMatchers("/admin/**").hasRole("ADMIN");

        //6. 组合校验
        http.authorizeRequests().antMatchers("/private/**").anonymous().antMatchers("/private/**").hasIpAddress("0:0:0:0:0:0:0:1");

        //7.强制使用HTTPS,若使用非HTTPS请求，则重定向到HTTPS上
        http.requiresChannel().antMatchers("/private/**").requiresSecure();

        //8.强制使用HTTP,若使用HTTPS请求首页，则将请求重定向到HTTP
        http.requiresChannel().antMatchers("/").requiresInsecure();


    }

    /**
     * 登陆认证方式配置示例
     */
    private void configure2(HttpSecurity http) throws Exception {
        //1. 这样配置的话，使用的是它自带的登陆页。/login get 请求为登陆页； /login post 请求为提交登陆表单
        http.formLogin().permitAll();

        //2. 注意如果改掉了默认登陆页，那么 /mylogin get 请求获取登陆页；/mylogin post 请求为提交表单；/mylogin?error 请求为验证失败 ：/mylogin?logout 为退出后重定向的请求
        http.formLogin().loginPage("/mylogin");

        //3. 如果想自定义表单提交路径，可以这样，其他的也一样
        http.formLogin().loginProcessingUrl("/dologin").loginPage("/loginpage");

        // 4. 记住我功能如果配合默认登陆页使用，则会自动添加一个 "rememberMe"参数，勾选并登陆成功后会在客户端生成一个14有效期的cookie
        http.rememberMe();

        // 5. 当然这个表单参数也可以自定义,还有cookie定制
        http.rememberMe().rememberMeParameter("formParamRememberMe").tokenValiditySeconds(60).rememberMeCookieName("myCookieName");
        // logout登出， / login？success，登出请求必须未post

        //6. 登出后清空session跳转到首页，如果登陆页地址改了，那么退出成功后的转发地址需要在这里配置，不然404
        http.logout().logoutSuccessUrl("/");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}