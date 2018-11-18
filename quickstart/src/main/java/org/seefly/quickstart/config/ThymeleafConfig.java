package org.seefly.quickstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * ThymeleafConfig配置类
 * 引入了Thymeleaf的Start可以不用这个配置类就可以直接使用
 * 这个配置类为了演示自定义配置
 * springMVC整合官方文档在这里：https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#integrating-thymeleaf-with-spring
 *
 * @author liujianxin
 * @date 2018-11-05 23:15
 */
@Configuration
public class ThymeleafConfig {
    /**
     *  视图解析器
     */
    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        // 优先级 这里有个坑，这个视图解析器的优先级要是比beanNameViewResolver高的话
        // 通过这个视图解析器没有找到对应的模板 会报错。坑爹
        return viewResolver;
    }
    /**
     * 模板引擎，植入模板解析
     */
    @Bean
    public TemplateEngine templateEngine(SpringResourceTemplateResolver resolver){
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        return  engine;
    }
    /**
     * 模板解析，配置模板位置，及后缀
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        // 这里有个坑，不用HTML5 用HTML的话，Html文件第一行必须要有 PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"
        // 百度一下<!DOCTYPE>去w3c看看就知道为啥了
        resolver.setTemplateMode("HTML5");
        return resolver;

    }
}
