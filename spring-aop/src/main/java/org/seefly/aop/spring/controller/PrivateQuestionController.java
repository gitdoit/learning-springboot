package org.seefly.aop.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.seefly.aop.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示aop + privateController时作用域的问题
 *
 *
 * @author liujianxin
 * @date 2021/4/20 22:12
 */
@Slf4j
@RestController
@RequestMapping("/proxy")
public class PrivateQuestionController {
    @Autowired
    protected UserService userService;
    
    /**
     * 私有方法被aop的同时还当作接口来使用
     * 如果没有aop，可以正常访问aopApplication属性
     * 有aop的话就不行
     * 原理请看ControllerAspect中的注解
     */
    @GetMapping("/private")
    private String privateApi(){
        log.info("aop+private api 中的this-->[{}]",this);
        log.info("aop+private api 中的注入成员变量-->[{}]",userService);
        return "ok";
    }
    
    @GetMapping("/public")
    public String publicApi(){
        log.info("aop+public api 中的this-->[{}]",this);
        log.info("aop+public api 中的注入成员变量-->[{}]",userService);
        return "public";
    }
}
