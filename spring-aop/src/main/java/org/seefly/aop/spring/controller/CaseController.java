package org.seefly.aop.spring.controller;

import org.seefly.aop.spring.model.User;
import org.seefly.aop.spring.service.AnnotationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示用例Demo
 * @author liujianxin
 * @date 2021/7/23 11:34
 **/
@RestController
@RequestMapping("/case")
public class CaseController {
    private final AnnotationService service;

    public CaseController( AnnotationService service ) {
        this.service = service;
    }

    @GetMapping("/anno")
    public String invokeAnnotationService(){
        return service.doSomething("do it");
    }

    @GetMapping("/spel")
    public String invokeBySpEl( User user){
        return service.getValueByKey(user);
    }
}
