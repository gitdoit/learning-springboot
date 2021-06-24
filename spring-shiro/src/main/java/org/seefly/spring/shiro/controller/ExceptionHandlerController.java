package org.seefly.spring.shiro.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liujianxin
 * @date 2021/6/8 17:55
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    
    @ExceptionHandler
    @ResponseBody
    public String errorHandler(AuthorizationException e) {
        log.error("没有通过权限验证！", e);
        return "没有通过权限验证！";
    }
}
