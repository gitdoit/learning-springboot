package org.seefly.springweb.controller.advice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2020/8/6 9:38
 */
@Controller
@RequestMapping("/attr")
/**
 * basePackages指定对某个包下生效
 */
@RestControllerAdvice(basePackages = "org.seefly.springweb.controller.advice")
public class ModelAttributeHandler {

    @ModelAttribute("params")
    public Map<String,String> someParam(){
        Map<String,String> paramFromSys = new HashMap<>();
        paramFromSys.put("nihao","nihao");
        return paramFromSys;
    }

    @GetMapping("/param")
    public String map(@ModelAttribute("params") Map<String,String> param){
        System.out.println(param);
        return "Ok";
    }
}
