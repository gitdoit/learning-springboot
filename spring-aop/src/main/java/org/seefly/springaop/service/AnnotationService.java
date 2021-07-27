package org.seefly.springaop.service;

import lombok.extern.slf4j.Slf4j;
import org.seefly.springaop.aspect.AspectWithAnnotation;
import org.seefly.springaop.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author liujianxin
 * @date 2021/7/23 11:39
 **/
@Slf4j
@Service
public class AnnotationService {
    public static final String A = "223";
    @Value("${server.port}")
    private String port;
    public String getPort(){
        return port;
    }

    @AspectWithAnnotation.DistributedLock
    public String doSomething(String value) {
        log.info("[AnnotationService] has been invoke,the value is [{}]",value);
        return "ok!";
    }

    // #{} 这种写法需要parser.parseExpression(spel, new TemplateParserContext());
    // 并且只能在#{}里面写表达式了
    @AspectWithAnnotation.DistributedLock(key = "#{ @annotationService.port + #user.name} ok!")
    public String getValueByKey( User user){
        return "ok!";
    }

}
