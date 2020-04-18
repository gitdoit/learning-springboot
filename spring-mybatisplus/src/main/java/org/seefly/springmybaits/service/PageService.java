package org.seefly.springmybaits.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.seefly.springmybaits.entity.Country;

/**
 * @author liujianxin
 * @date 2018-07-01 15:53
 **/
public interface PageService extends IService<Country> {

    IPage<Country> listByName(int index,int size,String name);
}
