package org.seefly.spring.shiro.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.seefly.spring.shiro.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liujianxin
 * @date 2021/6/8 17:24
 */
@Slf4j
@Controller
public class IndexController {
    
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    
    @ResponseBody
    @PostMapping("/login")
    public String login(@Validated @RequestBody User user) {
        //用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken( user.getUserName(), user.getPassword() );
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            log.error("用户名不存在！", e);
            return "用户名不存在！";
        } catch (AuthenticationException e) {
            log.error("账号或密码错误！", e);
            return "账号或密码错误！";
        } catch (AuthorizationException e) {
            log.error("没有权限！", e);
            return "没有权限";
        }
        return "login success";
    }
    
    @ResponseBody
    @GetMapping("/index")
    public String index(){
        return "This is index!";
    }
    
    @ResponseBody
    @GetMapping("/error")
    public String error(){
        return "登录失败!";
    }
    
}
