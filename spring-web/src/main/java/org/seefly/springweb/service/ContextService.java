package org.seefly.springweb.service;

import org.seefly.springweb.controller.FileUploadController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 传统的SpringMVC项目里面WebApplicationContext和ApplicationContext是有父子容器关系的
 * 就是Controller类型的Bean和Service类型的Bean在两个容器里
 * 即Controller在子容器，Service在父容器，子容器可以注入父容器的Bean，反过来则不行
 * 现在集成了SpringBoot则没有这个问题了，两个都在同一个容器里面。
 *
 * @author liujianxin
 * @date 2021/4/8 22:52
 */
@Service
public class ContextService {
    @Autowired
    private FileUploadController controller;
    
    @PostConstruct
    public void testOK(){
        System.out.println(controller);
    }
}
