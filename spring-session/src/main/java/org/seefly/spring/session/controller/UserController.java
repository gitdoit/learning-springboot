package org.seefly.spring.session.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author liujianxin
 * @date 2020/6/17 17:34
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    public String mySession(@PathVariable("id") String id, HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("fuck","ok");
        return "ok";
    }
}
