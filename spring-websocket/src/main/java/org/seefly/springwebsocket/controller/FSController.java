package org.seefly.springwebsocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liujianxin
 * @date 2019-02-26 16:15
 */
@Controller
public class FSController {

    @GetMapping("/fs")
    public String test(){
        return "/fs/training";
    }


}
