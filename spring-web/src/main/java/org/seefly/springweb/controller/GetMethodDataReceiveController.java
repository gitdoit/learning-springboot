package org.seefly.springweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

/**
 * 演示get方法请求后端接口时的参数接收姿势
 *
 * @author liujianxin
 * @date 2020/3/5 15:57
 */
@RestController
@RequestMapping("get")
public class GetMethodDataReceiveController {


    /**
     * 演示get方法传送数组
     * 前端  localhost:8080/getArray?item=fff,sss,fff
     * 【【【【如果要直接接收数组，不用类包装，则只能用数组，不能用列表】】】】
     * 另外这中传参方法是spring mvc的内部支持
     *
     * {@link RequestParamMethodArgumentResolver}
     */
    @GetMapping("/array")
    public String[] array(String[] item){
        System.out.println(item);
        return item;
    }

    /**
     * 演示从路径中绑定restful参数
     * {@link PathVariableMethodArgumentResolver}
     */
    @GetMapping("/restful/{id}")
    public String restful(@PathVariable("id") String restful){
        System.out.println(restful);
        return restful;
    }
}
