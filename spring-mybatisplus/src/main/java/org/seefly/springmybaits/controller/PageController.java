package org.seefly.springmybaits.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.seefly.springmybaits.entity.Country;
import org.seefly.springmybaits.service.PageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示在mybatis-plus中如何进行分页
 * @author liujianxin
 * @date 2018-07-01 15:47
 **/
@RestController
public class PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    /**
     * 使用mybatis-plus自带的接口进行分页
     */
    @GetMapping("/page")
    public IPage<Country> page(Integer index, Integer size) {
        IPage<Country> page = new Page<>(index,size);
        return pageService.page(page);
    }

    /**
     * 自己写xml进行分页
     */
    @GetMapping("/pageByName")
    public IPage<Country> pageByName(Integer current,Integer size,String name){
        return pageService.listByName(current,size,name);
    }

}
