package org.seefly.springweb01.log;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.seefly.springweb01.log.event.AuditEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Aspect
@Slf4j
public class AuditHandler implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;


    @Pointcut("@annotation(org.seefly.springweb01.log.AuditCut)")
    public void controllerPointCut() {
    }
    @Before("controllerPointCut() && @annotation(auditCut)")
    public void controller(JoinPoint pjp,AuditCut auditCut)  {
        AuditEvent auditEvent = new AuditEvent(this);
        // 平台
        auditEvent.setPlatform(auditCut.platform());
        // 调用的方法名称
        auditEvent.setFuncName(auditCut.funcName());
        // 请求者IP
        auditEvent.setIp(getIp());
        // TODO 用户信息
        Object[] args = pjp.getArgs();
        if (args != null && args.length > 0) {
            List<Object> list = new ArrayList<>();
            for (Object o : args) {
                if (o instanceof ServletRequest || o instanceof ServletResponse || o instanceof ServletConfig || o instanceof ServletContext) {
                    continue;
                }
                list.add(o);
            }
            String params = JSONObject.toJSONString(list);
            if(params.length()>2000){
                params = params.substring(0,2000);
            }
            auditEvent.setParams(params);
        }
        applicationEventPublisher.publishEvent(auditEvent);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher  = applicationEventPublisher;
    }

    private  static String getIp() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String ip = null;
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            ip = getIpAddr(request);
        }

        return ip;
    }

    private static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(",") > 0)
            ip = ip.split(",")[0];
        return ip;
    }
}
