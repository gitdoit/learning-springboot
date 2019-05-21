package org.seefly.springweb03.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liujianxin
 * @date 2018-11-27 21:46
 */
@Controller
@RequestMapping("/template")
public class QuickController {
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 基本的视图响应，响应一个无需渲染的html页面
     * grammar
     */
    @GetMapping("/index")
    public String index(){
        System.out.println("index request！");
        return "index";
    }

    /**
     * 简单thymeleaf语法演示
     */
    @GetMapping("/loginPage")
    public String getLoginPage(){
        MessageSource bean = applicationContext.getBean(MessageSource.class);
        Object messageSource = applicationContext.getBean("messageSource");
        System.out.println(messageSource);
        System.out.println(bean);
        return "login";
    }


}
