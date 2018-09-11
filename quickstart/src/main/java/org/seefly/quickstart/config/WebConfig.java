package org.seefly.quickstart.config;

import lombok.extern.slf4j.Slf4j;
import org.seefly.quickstart.anno.MyParamAnno;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-09-11 19:49
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 注入自定义方法参数解析器
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        /**
         * 自定以参数解析器，解析参数上标注有指定注解的
         */
        HandlerMethodArgumentResolver resolver = new HandlerMethodArgumentResolver(){
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(MyParamAnno.class);
            }
            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
                String paramValue =  request.getParameter(parameter.getParameterName());
                log.info("参数名称："+parameter.getParameterName());
                Object arg = null;
                if (paramValue != null) {
                    arg = paramValue+"liujianxinhahah";
                }
                log.info("请求值："+arg);
                return arg;
            }
        };
        resolvers.add(resolver);
    }
}
