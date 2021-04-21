package org.seefly.springaop.controller;

import org.seefly.springaop.SpringAopApplication;
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
@RestController
@RequestMapping("/proxy")
public class ProxyController {
    @Autowired
    protected SpringAopApplication aopApplication;
    
    /**
     * 私有方法被aop的同时还当作接口来使用
     * 如果没有aop，可以正常访问aopApplication属性
     * 有aop的话就不行
     * 原理请看ControllerAspect中的注解
     * @return
     */
    @GetMapping("/private")
    private String privateApi(){
        System.out.println(this);
        System.out.println(aopApplication);
        return "ok";
    }
    
    @GetMapping("/public")
    public String publicApi(){
        System.out.println(this);
        System.out.println(aopApplication);
        return "public";
    }
}
