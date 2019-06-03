package org.seefly.springweb01.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.InitializingBean;
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
import javax.validation.ConstraintViolationException;

/**
 *
 */
@Controller
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler, ApplicationEventPublisherAware,InitializingBean {
    private ApplicationEventPublisher applicationEventPublisher;




    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String runtimeExceptionHandler(HttpServletRequest request, Throwable throwable) {
        Integer code = 500;
        String msg = "网络异常！";

        // 断言异常
        if(throwable instanceof IllegalArgumentException ){
            msg = throwable.getMessage();
        }
        // 参数校验异常
        else if(throwable instanceof ConstraintViolationException){
            msg = "参数不合法:"+throwable.getMessage();
        }
        // 参数校验异常
        else if (throwable instanceof BindException){
            BindException ex = ((BindException)throwable);
            msg = "参数不合法:["+ex.getFieldError().getField()+":"+ex.getFieldError().getDefaultMessage()+"]";
        }
        // 参数校验异常
        else if (throwable instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException ex = ((MethodArgumentNotValidException)throwable);
            BindingResult result = ex.getBindingResult();
            msg = "参数不合法:["+result.getFieldError().getField()+":"+result.getFieldError().getDefaultMessage()+"]";
        }
        // 参数绑定异常
        else if(throwable instanceof MethodArgumentTypeMismatchException){
            MethodArgumentTypeMismatchException ex = ((MethodArgumentTypeMismatchException)throwable);
            String targetName = ex.getName();
            String targetType = ex.getParameter().getParameterType().getName();
            Object inputValue = ex.getValue();
            msg = "参数类型不匹配[参数 "+targetName+" 类型为"+targetType+",接收参数为"+inputValue+"]";
        }
        log.error("",throwable);
        return msg;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        for (StackTraceElement traceElement : stackTrace){
            System.out.println(traceElement.getMethodName());
        }
        e.printStackTrace();
        System.out.println("异常！！");
        Message message = new Message("A","B",e.getMessage().getBytes());
    }



    @Override
    public void afterPropertiesSet(){
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
