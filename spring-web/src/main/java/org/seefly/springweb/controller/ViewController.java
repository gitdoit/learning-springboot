package org.seefly.springweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liujianxin
 * @date 2021/4/8 21:37
 */
@Controller
@RequestMapping("view")
public class ViewController {
    
    @GetMapping("/index")
    public String index(){
        return "index";
    }
    
}
