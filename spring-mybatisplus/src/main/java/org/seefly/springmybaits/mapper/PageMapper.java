package org.seefly.springmybaits.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.seefly.springmybaits.entity.Country;


/**
 * @author liujianxin
 * @date 2018-07-01 15:55
 * 描述信息：
 **/
public interface PageMapper extends BaseMapper<Country> {

    IPage<Country> listByName(IPage<?> page, String name);
}
