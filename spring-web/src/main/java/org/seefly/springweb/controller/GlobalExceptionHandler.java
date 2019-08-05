package org.seefly.springweb.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.seefly.springweb.controller.response.Response;
import org.seefly.springweb.controller.response.ResponseUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * 该类处理全局的Controller层抛出的异常，将其包装，按照约定，返回统一的编码
 * 对于多线程未处理的异常，也将在这里进行捕获
 *
 * 对于捕获的所有异常都将发送到消息队列，在日志处理服务统一处理。
 * @author liujianxin
 */
@Slf4j
@Controller
@ControllerAdvice
public class GlobalExceptionHandler implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    public GlobalExceptionHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Controller全局异常捕获
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Response runtimeExceptionHandler(HttpServletRequest request, Throwable throwable) {
        Integer code = 500;
        String msg;
        if(throwable instanceof IllegalArgumentException ){
            msg = throwable.getMessage();
        }
        // 参数校验异常
        else if(throwable instanceof ConstraintViolationException){
            ConstraintViolationException cve = (ConstraintViolationException)throwable;
            ConstraintViolation<?> next = cve.getConstraintViolations().iterator().next();
            msg = next.getMessage();
        }
        // 参数校验异常
        else if (throwable instanceof BindException){
            BindException ex = ((BindException)throwable);
            msg = ex.getFieldError().getDefaultMessage();
        }
        // 参数校验异常
        else if (throwable instanceof MethodArgumentNotValidException ){
            MethodArgumentNotValidException ex = ((MethodArgumentNotValidException)throwable);
            BindingResult result = ex.getBindingResult();
            msg = result.getFieldError().getDefaultMessage();
        }
        // 参数绑定异常
        else if(throwable instanceof MethodArgumentTypeMismatchException){
            MethodArgumentTypeMismatchException ex = ((MethodArgumentTypeMismatchException)throwable);
            String targetName = ex.getName();
            String targetType = ex.getParameter().getParameterType().getName();
            Object inputValue = ex.getValue();
            msg = "参数类型不匹配[参数 "+targetName+" 类型为"+targetType+",接收参数为"+inputValue+"]";
        } else {
            if (throwable.getLocalizedMessage() == null || throwable.getLocalizedMessage().length() > 20) {
                msg = "网络异常";
            } else {
                msg = throwable.getLocalizedMessage();
            }
        }
        log.error("",throwable);
        return ResponseUtils.error(code,msg);
    }



    private String getStackTrace(Throwable throwable){
        String stackTrace = null;
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            pw.flush();
            sw.flush();
            stackTrace = sw.toString();
        } catch (IOException e) {
            log.warn("记录异常日志信息发生错误！", e);
        }
        return stackTrace;
    }

    private String getHeaders(HttpServletRequest request){
        Map<String, List<String>> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            Enumeration<String> headers = request.getHeaders(name);
            while (headers.hasMoreElements()){
                headersMap.computeIfAbsent(name,k -> new ArrayList<>()).add(headers.nextElement());
            }
        }
        return  JSONObject.toJSONString(headersMap);
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
