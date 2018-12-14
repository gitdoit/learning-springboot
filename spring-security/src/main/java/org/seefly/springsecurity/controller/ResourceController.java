package org.seefly.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujianxin
 * @date 2018-12-12 15:08
 */
@RestController
@RequestMapping("/private")
public class ResourceController {

    @GetMapping("/vip1")
    public String vip1(){
        return "private vip1权限";
    }

    @GetMapping("/vip2")
    public String vip2(){
        return "private vip2权限";
    }
}
