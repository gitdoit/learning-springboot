package org.seefly.springweb.handle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 这种方式也能注册一个handle，但是SpringMVC没有默认提供一个
 * 处理器适配器，所以要我们手动来一个
 * @author liujianxin
 * @date 2020/7/31 15:28
 */
//@WebServlet("/httpservlet")
@Controller("/httpservlet")
public class ExtendServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("http servlet...");
    }

    @Configuration
    public static class Config{
        @Bean
        public SimpleServletHandlerAdapter simpleServletHandlerAdapter() {
            return new SimpleServletHandlerAdapter();
        }
    }


}
