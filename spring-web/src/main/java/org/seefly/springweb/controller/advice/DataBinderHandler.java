package org.seefly.springweb.controller.advice;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liujianxin
 * @date 2020/8/6 9:15
 */
@Controller
@RequestMapping("/binder")
/**
 * basePackages指定对某个包下生效
 */
@RestControllerAdvice(basePackages = "org.seefly.springweb.controller.advice")
public class DataBinderHandler {

    @GetMapping("/date")
    public String testDateBinder(Date date){
        System.out.println(date);
        return "ok";
    }


    /**
     * 定义对日期格式的数据绑定
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        // CustomDateEditor 有很多相同性质的类，都是用来处理各种参数类型绑定的
        // 例如对Number类型，数组类型； 详细见 PropertyEditorSupport 的子类
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }


}
