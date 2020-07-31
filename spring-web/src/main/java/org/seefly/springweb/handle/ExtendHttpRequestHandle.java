package org.seefly.springweb.handle;

import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * org.springframework.web.servlet.resource.ResourceHttpRequestHandler
 * 也是这个类的实现类，用来处理静态资源
 * @author liujianxin
 * @date 2020/7/31 15:15
 */
@Controller("/requestHandle")
public class ExtendHttpRequestHandle implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("HttpRequestHandle....");
    }
}
