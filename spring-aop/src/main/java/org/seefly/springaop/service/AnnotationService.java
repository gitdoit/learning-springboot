package org.seefly.springaop.service;

import lombok.extern.slf4j.Slf4j;
import org.seefly.springaop.aspect.AspectWithAnnotation;
import org.seefly.springaop.model.User;
import org.springframework.stereotype.Service;

/**
 * @author liujianxin
 * @date 2021/7/23 11:39
 **/
@Slf4j
@Service
public class AnnotationService {
    public static final String A = "223";

    @AspectWithAnnotation.DistributedLock
    public String doSomething(String value) {
        log.info("[AnnotationService] has been invoke,the value is [{}]",value);
        return "ok!";
    }

    @AspectWithAnnotation.DistributedLock(key = "@annotationService.A + #user.name")
    public String getValueByKey( User user){
        return "ok!";
    }

}
