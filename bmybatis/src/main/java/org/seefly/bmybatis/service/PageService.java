package org.seefly.bmybatis.service;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.seefly.bmybatis.model.Country;

/**
 * @author liujianxin
 * @date 2018-07-01 15:53
 * 描述信息：
 **/
public interface PageService {
    PageInfo<Country> getCountryByPage(RowBounds rowBounds);

    PageInfo<Country> getCountryByPage(Integer index, Integer size);
}
