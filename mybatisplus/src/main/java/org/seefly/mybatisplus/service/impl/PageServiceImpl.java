package org.seefly.mybatisplus.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.seefly.mybatisplus.mapper.PageMapper;
import org.seefly.mybatisplus.model.Country;
import org.seefly.mybatisplus.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-07-01 15:53
 * 描述信息：
 **/
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private PageMapper pageMapper;

    /**
     * 使用RowBounds
     * @param rowBounds
     * @return
     */
    @Override
    public PageInfo<Country> getCountryByPage(RowBounds rowBounds) {
        List<Country> list = pageMapper.getCountryByPage(rowBounds);
        return new PageInfo<>(list);
    }

    /**
     * 使用PageHelper.start
     * @return
     */
    @Override
    public PageInfo<Country> getCountryByPage(Integer index,Integer size) {
        PageHelper.startPage(index,size);
        return new PageInfo<>(pageMapper.CountryByPage());
    }
}
