package org.seefly.springweb.convert;


import lombok.extern.slf4j.Slf4j;
import org.seefly.springweb.controller.request.UserRequest;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


/**
 * 由于标注的@Component注解，会被扫描装配到容器中。
 * 然后{@link WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#addFormatters}
 *
 * 这个Converter不能解析被@RequestBody标注的参数
 * 因为一旦被这个注解标注，那就使用Json来转了
 *
 *
 * @author liujianxin
 * @date 2018-07-06 00:00
 **/
@Slf4j
@Component
public class String2UserConverter implements Converter<String, UserRequest> {


    @Override
    public UserRequest convert(String source) {
        log.info("String -> UserRequest [{}]",source);
        String[] split = source.split("#");
        String name = split[0];
        Integer age = Integer.valueOf(split[1]);
        return new UserRequest(name,age);
    }
}
