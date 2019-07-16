package org.seefly.springweb.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.seefly.springweb.component.AnnoArgumentResolver;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2018-09-11 19:49
 * 对于@EnableWebMvc注解
 * 当在配置类或者启动类上标注这个注解的时候
 * springboot自动配置类{@link WebMvcAutoConfiguration}中的自动配置将会失效
 * 例如静态资源映射这些都没有了，也就是说这个注解表示全面接管web方面的配置
 * 此时生效的只有{@link DelegatingWebMvcConfiguration}类中的配置
 *
 * 国际化：{@link MessageSourceAutoConfiguration}
 */

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {



    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //路径映射视图，省的在Controller中写一个空方法返回视图了
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
    }

    /**
     *
     *
     *
     * 添加自定义参数解析器，用于从请求中获取数据将之绑定到对应的接口参数中
     * 对于{@link HandlerMethodArgumentResolver}接口有两个方法
     * supportsParameter  定义能解析哪些参数
     * resolveArgument    定义如何解析
     *
     * 将被配置到->{@link RequestMappingHandlerAdapter#afterPropertiesSet()}
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AnnoArgumentResolver());
    }

    /**
     * 添加返回值处理器,对于{@link HandlerMethodReturnValueHandler}接口有两个方法
     * supportsReturnType  定义能够解析哪种类型的返回值
     * handleReturnValue   定义如何解析
     *
     * 将被配置到->{@link RequestMappingHandlerAdapter#afterPropertiesSet()}
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

    }
    /**
     * 消息转换器，用来解析请求体中的消息，和向响应体中写入消息；
     * 对于标注了@RequestBody的参数，和@ResponseBody的响应，都会被{@link RequestResponseBodyMethodProcessor}处理
     * 因为这个类实现了解析请求参数{@link HandlerMethodArgumentResolver}、{@link HandlerMethodReturnValueHandler}
     * 且只处理标注了这两个注解的参数解析，和返回值处理，但是实际干活的还是这个消息转换器。
     *
     * 对于没有标注@RequestBody的参数(不是从请求体中读消息)，一般使用{@link RequestParamMapMethodArgumentResolver}进行参数绑定
     * 对于没有标注@ResponseBody的接口，那就需要视图解析器返回对应的视图了
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converterf = new FastJsonHttpMessageConverter();
        MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter(new Jaxb2Marshaller());
        converters.add(converter);
        converters.add(converterf);
    }


    /**
     * 详情：https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-pathmatch
     * 内容协商配置，用来确定响应内容的格式
     * 由于理想情况下是根据请求头中的  Accept:application/json...... 字段来告诉服务器
     * 客户端想要接收什么样类型的消息内容，但实际情况是客户端一般都处理不好这个请求头。导致返回的内容并不是客户端真正想要的
     * 所以spring提供了多种策略用来确定响应内容格式，但这些策略需要手动开启
     * 1、URL中的拓展后缀如 ：http://www.abc.com/book/spring.xml（{@link PathExtensionContentNegotiationStrategy}）
     * 2、通过RUL参数确定如：http://www.abc.com/book/spring?format=xml ({@link ParameterContentNegotiationStrategy})
     * 3、通过请求头中的 Accept 如：Accept:application/json  （{@link HeaderContentNegotiationStrategy}）
     * 4、固定的响应格式（{@link FixedContentNegotiationStrategy}）
     * .....
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        Map<String,MediaType> map = new HashMap<>(2);
        map.put("json",MediaType.APPLICATION_JSON);
        map.put("xml",MediaType.APPLICATION_XML);

        configurer.
                // 默认的响应体内容格式
                        defaultContentType(MediaType.APPLICATION_JSON)
                // 开启后缀匹配
                .favorPathExtension(true)
                // 指定的后缀到媒体类型映射，你也可以添加 pdf -> application/pdf映射
                .mediaTypes(map)
                // 忽略请求头中的Accept
                .ignoreAcceptHeader(true)
                // 关闭参数匹配
                .favorParameter(false);
    }

    /**
     * 详情
     * https://docs.spring.io/spring/docs/5.0.7.RELEASE/spring-framework-reference/web.html#mvc-ann-requestmapping-suffix-pattern-match
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                //If enabled a method mapped to "/users" also matches to "/users.*".
                .setUseSuffixPatternMatch(true)
                //If enabled a method mapped to "/users" also matches to "/users/".
                .setUseTrailingSlashMatch(false)
                //后缀模式匹配是否仅适用于配置内容协商时显式注册的路径扩展。
                .setUseRegisteredSuffixPatternMatch(false)
                .setPathMatcher(new AntPathMatcher())
                .setUrlPathHelper(new UrlPathHelper());
    }


    /**
     * 配置视图解析器
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        /*
         * 启用内容协商，并设置视图类型
         * 当请求为 http://...../book.json   ->  响应json格式数据
         *         http://...../book.xml    ->  响应xml格式数据
         * 当然要指定对应的视图
         *
         * ViewResolver的实现，它根据请求文件名或Accept标头解析视图。
         * ContentNegotiatingViewResolver本身不解析视图，但委托给其他ViewResolvers。
         * 默认情况下，这些其他视图解析器会自动从应用程序上下文中选取，但也可以使用viewResolvers属性显式设置。
         * 请注意，为了使此视图解析器正常工作，需要将order属性设置为高于其他属性（默认值为Ordered.HIGHEST_PRECEDENCE）。
         * 此视图解析程序使用请求的媒体类型为请求选择合适的视图。, 请求的媒体类型通过配置的ContentNegotiationManager确定。,
         * 确定所请求的媒体类型后，此解析程序将查询每个代理视图解析程序以查看视图，并确定所请求的媒体类型是否与视图的内容类型兼容）。
         * 返回最兼容的视图。, 此外，此视图解析器公开defaultViews属性，允许您覆盖视图解析器提供的视图。
         * 请注意，这些默认视图是作为候选者提供的，并且仍然需要具有所请求的内容类型（通过文件扩展名，参数或Accept标头，如上所述）。
         * 例如，如果请求路径是/view.html，则此视图解析程序将查找具有text / html内容类型的视图（基于html文件扩展名）。
         * 带有text / html请求的/ view请求Accept header具有相同的结果
         * @param manager
         * @return
         */
        // Jaxb2Marshaller xml = new Jaxb2Marshaller();
        //xml.setPackagesToScan("org.seefly.quickstart.domain");
        //xml.setClassesToBeBound(RestBean.class); ,new MarshallingView(xml)
        //启用这个thymeleaf不能用了
        //registry.enableContentNegotiation(new MappingJackson2JsonView());
        //开启beanName视图解析，省的自己创建这个实例放到容器里了
        registry.beanName();
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


}
