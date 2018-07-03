package org.seefly.mybatisplus.controller;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.seefly.mybatisplus.model.Country;
import org.seefly.mybatisplus.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liujianxin
 * @date 2018-07-01 15:47
 * 描述信息：学习使用分页插件
 **/
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 使用RowBounds配合PageHelper进行分页
     * RowBounds传到Mapper的时候PageHepler会进行拦截，发现了RowBounds则会认为
     * 这次请求是一个分页请求，自动生成查询count的sql并在查询List结果集之前执行。
     * 将查询到的count数封装到list（list实际类型是Page）中
     * 然后在执行我们写的sql语句（sql语句不要自己加limit 拦截器会自动帮我们加），查询数据集合
     * 再封装到page中，这些操作中使用了ThreadLoal，通过这个ThreadLocal将一次请求（同一个线程）的两次查询
     * 的结果封装到一个page中
     * @param index
     * @param size
     * @return
     */
    @RequestMapping("/row")
    @ResponseBody
    public PageInfo<Country> page(Integer index, Integer size){
        return pageService.getCountryByPage(new RowBounds(index,size));
    }

    @RequestMapping("/start")
    @ResponseBody
    public PageInfo<Country> pageP(Integer index, Integer size){
        return pageService.getCountryByPage(index,size);
    }
}
