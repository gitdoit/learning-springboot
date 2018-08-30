package org.seefly.quickstart.config;

import lombok.extern.slf4j.Slf4j;
import org.seefly.quickstart.anno.MyParamAnno;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2018-07-13 09:37
 * 描述信息：
 **/
@Slf4j
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        HandlerMethodArgumentResolver resolver = new HandlerMethodArgumentResolver(){
            /**
             * 可以处理带有@MyparamAnno注解的参数解析绑定
             * @param parameter
             * @return
             */
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
                    arg = paramValue+"liujianxinniubi";
                }
                log.info("请求值："+arg);
                return arg;
            }
        };
        argumentResolvers.add(resolver);
        super.addArgumentResolvers(argumentResolvers);
    }

    /**
     * 添加自定义的消息转换器
     * 当请求头中的Accept=liujianxin/haoniubi;charset=utf-8的时候
     * 将会使用我这个转换器进行转换
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        List<MediaType> list = Collections.emptyList();
        list.add(new MediaType("liujianxin","haoniubi",StandardCharsets.UTF_8));
        converter.setSupportedMediaTypes(list);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }



    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 设置默认响应类型,当没有从后缀，请求头，参数中获取到响应类型时默认返回json格式
        //configurer.defaultContentType(MediaType.APPLICATION_JSON);
        // 忽略请求头中的响应类型
        //configurer.ignoreAcceptHeader(true);
        // application/json
        //configurer.mediaType("json",MediaType.APPLICATION_JSON);
    }

    @Bean
    public ViewResolver cnViewResolver(ContentNegotiationManager manager){
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setContentNegotiationManager(manager);
        return viewResolver;
    }

    /********************************************BeanNameViewResolver - 视图解析器*************************************************************************************/

    /**
     * beanNameViewResolver的任务就是将视图解析为Spring应用上下文中 的bean
     * 其中bean的id与视图名字相同
     * @return
     */
    @Bean
    public ViewResolver beanNameResolver(){
        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
        beanNameViewResolver.setOrder(2);
        return beanNameViewResolver;
    }

    /**
     * 由于配置了beanNameViewResolver，且这里也注册了一个View的实例
     * 那么在Controller中返回这个bean的id(spittles)时，将会使用这个视图来处理响应
     *
     * @return
     */
    @Bean
    public View spittles(){
        return new MappingJackson2JsonView();
    }

    /**
     * 自定义View
     * View接口的任务就是接收模型以及Srevlet的request和response对象，并将输出渲染到response中
     * @return
     */
    @Bean
    public View helloView(){
        return new View(){
            @Override
            public String getContentType() {
                return "text/html";
            }
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.getWriter().println("heller world");
            }
        };
    }

/********************************************Thymeleaf - 视图解析器****************************************************************************************/

    /**
     *  视图解析器
     * @return
     */
    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        // 优先级 这里有个坑，这个视图解析器的优先级要是比beanNameViewResolver高的话
        // 通过这个视图解析器没有找到对应的模板 会报错。坑爹
        viewResolver.setOrder(5);
        return viewResolver;
    }
    /**
     * 模板引擎，植入模板解析
     * @param resolver
     * @return
     */
    @Bean
    public TemplateEngine templateEngine(ITemplateResolver resolver){
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        return  engine;
    }
    /**
     * 模板解析，配置模板位置，及后缀
     * @return
     */
    @Bean
    public ITemplateResolver templateResolver(){
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
         resolver.setPrefix("classpath:/abcd/");
         resolver.setSuffix(".html");
         // 这里有个坑，不用HTML5 用HTML的话，Html文件第一行必须要有 PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"
        // 百度一下<!DOCTYPE>去w3c看看就知道为啥了
         resolver.setTemplateMode("HTML5");
        return resolver;

    }

 /**********************************************************************************************************************************************/

    /**
     * 静态资源处理器
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


}
