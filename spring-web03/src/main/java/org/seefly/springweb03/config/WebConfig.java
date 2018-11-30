package org.seefly.springweb03.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @author liujianxin
 * @date 2018-11-27 20:11
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/conf").setViewName("/view");
    }



    /**
     * 国际化,这里自动配置
     * 自动配置的国际化解析器是根据请求头中的Accept-Language来选择的
     * 现在自定义配置解析请求参数中的国际化参数来选择国际化配置
     * {@link WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#localeResolver()}
     */
    //@Bean
    public LocaleResolver localeResolver(){
        return new LocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String local = request.getParameter("l");
                if(StringUtils.isEmpty(local)){
                    return request.getLocale() == null ? request.getLocale() : Locale.getDefault();
                }
                String[] params = local.split("_");
                return new Locale(params[0],params[1]);
            }
            @Override
            public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

            }
        };
    }



    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        //如果设置为-1，表示Cache forever。一般生产环境下采用-1，开发环境为了方便调测采用某个正整数，规范地我们可通过profile来定义
        messageSource.setCacheSeconds(5);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames("classpath:i18n/login");
        return messageSource;
    }




}
