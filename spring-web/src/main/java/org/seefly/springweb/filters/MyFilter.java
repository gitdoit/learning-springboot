package org.seefly.springweb.filters;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.xml.ws.WebFault;
import java.io.IOException;

/**
 * @author liujianxin
 * @date 2021/4/8 22:21
 */


@Order(-1)
@WebFilter(filterName = "filterDemo", urlPatterns = { "/*" }, initParams = { @WebInitParam(name = "name", value = "xc"),
        @WebInitParam(name = "like", value = "java") })
public class MyFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilter initing...");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("Request process by MyFilter...");
        chain.doFilter(request,response);
    }
    
    @Override
    public void destroy() {
        System.out.println("MyFilter destroy");
    }
}
