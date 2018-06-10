package org.seefly.quickstart.web;

import org.seefly.quickstart.domain.Peo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liujianxin
 * @date 2018-06-09 15:35
 * 描述信息：
 **/
@Controller
public class IndexController {

    @Autowired
    private Peo p;

    @ResponseBody
    @RequestMapping("/index")
    public String helloWorld(){
        return "hello world"+p;
    }
}
