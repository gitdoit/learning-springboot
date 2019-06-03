package org.seefly.springweb01.spring.component;

import org.seefly.springweb01.spring.annotation.MyParamAnno;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 注入自定义方法参数解析器
 * 在该类的{@link org.springframework.web.method.support.InvocableHandlerMethod#getMethodArgumentValues(NativeWebRequest, ModelAndViewContainer, Object...)}
 * 中选择合适的参数解析器，对于使用了{@link MyParamAnno}的注解，这里定义的解析器会进行参数处理。
 * @author liujianxin
 **/
public class AnnoArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MyParamAnno.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest request, WebDataBinderFactory binderFactory) {
        String paramValue =  request.getParameter(parameter.getParameterName());
        Object arg = null;
        if (StringUtils.isEmpty(paramValue)) {
            arg = paramValue+"liujianxinhahah";
        }
        return arg;
    }
}
