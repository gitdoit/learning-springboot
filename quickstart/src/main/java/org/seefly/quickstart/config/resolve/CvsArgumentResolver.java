package org.seefly.quickstart.config.resolve;

import com.opencsv.bean.CsvToBeanBuilder;
import org.seefly.quickstart.anno.BoxMessage;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 首先可以确定的是请求都是post
 * 其次参数值可能会从上传的文件中获取，可能会从requestBody那
 * 参数类型可以拿到，主要就是解析。不确定因素--源
 * @author liujianxin
 * @date 2018-09-13 20:54
 */
public class CvsArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(BoxMessage.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 首先拿到请求值 multi请求对应post请求，get请求 就是 ServletWebRequest
        if(webRequest instanceof ServletWebRequest){
            ServletWebRequest req = (ServletWebRequest)webRequest;
            if(req.getRequest() instanceof MultipartHttpServletRequest){
                MultipartHttpServletRequest rr = (MultipartHttpServletRequest) req.getRequest();
                MultipartFile filedata = rr.getFile("filedata");
                Class<?> parameterType = parameter.getParameterType();
                return new CsvToBeanBuilder<>(new InputStreamReader(filedata.getInputStream())).withType(parameterType).build().parse();
            }else {

            }
        }
        // 其次拿到参数类型
        return null;

    }
}
