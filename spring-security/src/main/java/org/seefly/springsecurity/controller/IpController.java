package org.seefly.springsecurity.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 受限资源，使用自定义IP登陆
 * @author liujianxin
 * @date 2018-12-17 19:14
 */
@RestController
@RequestMapping("/ip")
public class IpController {

    @RequestMapping("/show")
    public String ip(HttpServletRequest request){
        return "你的IP是:"+request.getRemoteHost();
    }
}
