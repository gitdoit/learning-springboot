package org.seefly.quickstart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liujianxin
 * @date 2018-11-18 16:11
 */
@Controller
public class InternationalController {

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }

    @RequestMapping("/bean")
    public String beanName(){
        return "helloView";
    }
}
