package org.seefly.springweb01.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
        // ServletException
        // HttpMessageConversionException 没有消息体
        // HttpMediaTypeNotSupportedException 请求体类型不支持
        throwable.printStackTrace();
        return "ERROR";
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
