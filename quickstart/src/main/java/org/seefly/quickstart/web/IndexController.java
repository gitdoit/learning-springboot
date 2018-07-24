package org.seefly.quickstart.web;

import org.seefly.quickstart.domain.PeoPor;
import org.seefly.quickstart.domain.RestBean;
import org.seefly.quickstart.request.TestReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author liujianxin
 * @date 2018-06-09 15:35
 * 描述信息：
 **/
@Controller
public class IndexController {

    @Autowired
    private PeoPor peoPor;

    @ResponseBody
    @RequestMapping("/index")
    public String helloWorld(){
        return "hello world"+peoPor;
    }

    @RequestMapping("/hello")
    public String page(){
        return "nihao";
    }

    /**
     * 测试接收列表数据
     * 前端发送：id[0]=11?id[1]=2
     * 后端使用List<Integer>接收
     * @param req
     * @return
     */
    @PostMapping("/haha")
    @ResponseBody
    public String batch(TestReq req){
        System.out.println(req);
        return "ok";
    }

    @RequestMapping("/bean")
    public String beanName(){
        System.out.println("beanmae");
        return "helloView";
    }


    @RequestMapping("/d")
    @ResponseBody
    public String date(Date date){
        System.out.println(date);
        return "ok";
    }


    @RequestMapping("/rest")
    public String rest(ModelMap modelMap){
        RestBean bean = new RestBean();
        modelMap.addAttribute("bean",bean);
        return "spittles";
    }
}
