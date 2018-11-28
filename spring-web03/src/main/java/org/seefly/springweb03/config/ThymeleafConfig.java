package org.seefly.springweb03.config;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

/**
 * ThymeleafConfig配置类，这个配置类为了演示代码方式自定义配置，代替配置文件配置。
 * 引入了Thymeleaf的Start可以不用这个配置类就可以直接使用,自动配置详见{@link ThymeleafAutoConfiguration}
 * Thymeleaf的配置属性详见{@link ThymeleafProperties}
 *
 * @author liujianxin
 * @date 2018-11-05 23:15
 * @see <a href = "https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#integrating-thymeleaf-with-spring">springMVC整合官方文档在这里</a>
 */
@Configuration
public class ThymeleafConfig {

    /**
     * 模板解析，配置模板位置，及后缀。
     * 自动配置类中{@link ThymeleafAutoConfiguration.DefaultTemplateResolverConfiguration}的注册条件
     * 是没有ID为“defaultTemplateResolver”的组件。这里若不指定id，则会配置两个模板资源解析器。
     */
    @Bean(name = "defaultTemplateResolver")
    public SpringResourceTemplateResolver templateResolver(ApplicationContext applicationContext) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        // 模板前缀及后缀
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        // 设置模板类型，并强制使用
        resolver.setTemplateMode(TemplateMode.HTML);
        //resolver.setForceTemplateMode(true);
        // 以指定编码解析模板
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // 开发阶段，不缓存。修改完HTML之后不用重启项目，idea上ctrl+f9实时刷新
        resolver.setCacheable(false);
        return resolver;
    }

    /**
     * 模板引擎，植入模板解析
     * 自动配置{@link ThymeleafAutoConfiguration.ThymeleafDefaultConfiguration#templateEngine()}
     */
    @Bean(name ="springTemplateEngine")
    public TemplateEngine templateEngine(SpringResourceTemplateResolver resolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }

    /**
     * 视图解析器
     * 自动配置详见{@link ThymeleafAutoConfiguration.ThymeleafWebMvcConfiguration.ThymeleafViewResolverConfiguration#thymeleafViewResolver()}
     */
    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        // 配置模板引擎
        viewResolver.setTemplateEngine(templateEngine);
        // 是否一边渲染模板一边将渲染结果放置发送缓冲区，默认true，可以显著提高性能。
        viewResolver.setProducePartialOutputWhileProcessing(true);
        // 视图解析器优先级，在多个视图解析器下有意义
        viewResolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
        // 开发阶段不缓存，相当于设置viewResolver.setCacheLimit(0)
        viewResolver.setCache(false);
        return viewResolver;
    }


}
