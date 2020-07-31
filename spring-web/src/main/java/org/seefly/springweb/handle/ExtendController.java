package org.seefly.springweb.handle;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * https://cloud.tencent.com/developer/article/1497623
 * @author liujianxin
 * @date 2020/7/31 14:55
 */
@Component("/beanName")
public class ExtendController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().write("ok fuck");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setStatus(HttpStatus.OK);
        return null;
    }
}
