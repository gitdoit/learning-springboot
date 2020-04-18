package org.seefly.springmybaits.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.seefly.springmybaits.entity.Country;
import org.seefly.springmybaits.mapper.PageMapper;
import org.seefly.springmybaits.service.PageService;
import org.springframework.stereotype.Service;

/**
 * @author liujianxin
 * @date 2018-07-01 15:53
 **/
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Country> implements PageService {

    private final PageMapper pageMapper;

    public PageServiceImpl(PageMapper pageMapper) {
        this.pageMapper = pageMapper;
    }


    @Override
    public IPage<Country> listByName(int current,int size,String name) {
        IPage<Country> page = new Page<>(current,size);
        return pageMapper.listByName(page,name);
    }
}
