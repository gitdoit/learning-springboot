package org.seefly.springweb.controller;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;

/**
 * Web接口参数校验
 *  TODO 组合校验
 * @author liujianxin
 * @date 2019-05-05 15:47
 */
@RestController
@Validated
public class ValidateController {


    /**
     * 使用基本类型接收参数，使用注解校验
     * 需要在类上添加@Validated注解才能生效
     */
    @GetMapping("/int")
    public String bindInt( @Max(5) Integer age){
        System.out.println(age);
        return "OK";
    }


    /**
     * 使用JavaBean来接收参数，在参数前添加@Validated注解
     * 并在该JavaBean的属性上添加对应的校验注解才能生效
     * 参数校验不通过时直接抛出BindException
     */
    @GetMapping("/people")
    public String people(@Validated People people){
        System.out.println(people);
        return "OK";
    }

    /**
     * 和上面一样，但是多了一个BeanPropertyBindingResult参数
     * 这个参数需要紧跟需要校验的参数后面，当校验不通过时将错误信息放到这个参数里面
     * 然后就不抛异常了。
     */
    @GetMapping("/bind")
    public String bindException(@Validated People people, BeanPropertyBindingResult result){
        System.out.println("参数不合法:["+result.getFieldError().getField()+":"+result.getFieldError().getDefaultMessage()+"]");
        return "OK";
    }

    @Data
    private static class People{
        @Length(max = 3)
        private String name;
        private Integer age;
    }
}
