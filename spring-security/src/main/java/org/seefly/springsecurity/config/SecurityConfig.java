package org.seefly.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
 * 关于requestMatchers的使用说明
 * <a href="https://stackoverflow.com/questions/38527723/what-is-the-reason-to-use-requestmatchers-antmatchers-without-a-verb-in-spri"/>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * 对登陆用户进行校验管理，设置对登陆用户的身份校验方式
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //基于jdbc的验证，并可以重写查询用户及权限的语句,设置密码加密，数据库中存储加密后的密码，前端密码传入后加密进行比较
        //auth.jdbcAuthentication().dataSource().usersByUsernameQuery().authoritiesByUsernameQuery().passwordEncoder()

        //基于内存认证
        /*auth.inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .authorities("ROLE_USER");
         等同于
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER");

         roles()方法会自动添加前缀ROLE_
        */
        /**
         * 这种方式可以提供一个service，实现一个根据用户名查询用户信息的接口，具体的用户信息是从哪里查询可以自己定义
         * 这样可以从redis中查询用户信息
         */
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
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/private/**").authenticated();
        http.formLogin().permitAll();
        //http.formLogin().loginPage("/login").failureForwardUrl("/");

        // logout登出，清空session并重定向到 / login？success，登出请求必须未post
        http.logout().logoutSuccessUrl("/login");

        // 对 "/oauth/**","/login/**","/logout/**","/abc/**" 路径进行配置，不影响除此之外的其他路径
        /*http.
                requestMatchers().
                    // 对这些集合中的路径进行配置，不影响除此之外的路径权限
                    antMatchers("/oauth/**","/login/**","/logout/**","/haha/**")
                .and()
                    // 对上面筛选出来的集合做进一步规则定制
                    .authorizeRequests()
                    // 对上面筛选出来的子集 "/oauth/**" 定制规则
                    .antMatchers("/oauth/**").authenticated()
                    .antMatchers("/login/**","/logout/**").permitAll()
                    // 这个配置是无效的，因为 这个路径不被包含在 requestMatchers().antMatchers 指定的集合中
                    .antMatchers("/abc/**").denyAll()
                    // 其他请求都拒绝，这个例子中只影响到了 /haha/**  对于没有在requestMatchers().antMatchers中指定的路径都不会影响到
                    .anyRequest().denyAll();*/

    }



    /**
     * 基本使用方法
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
     * 两种认证方式
     * @param http
     * @throws Exception
     */
    private void configure2(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/myLogin").
                and().httpBasic().realmName("sdf");
        //记住我
        http.rememberMe().key("mykey").tokenValiditySeconds(60*60*3);
        //登出后跳转到首页
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