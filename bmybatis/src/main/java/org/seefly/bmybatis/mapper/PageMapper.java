package org.seefly.bmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.session.RowBounds;
import org.seefly.bmybatis.model.Country;

import java.util.List;


/**
 * @author liujianxin
 * @date 2018-07-01 15:55
 * 描述信息：
 **/
public interface PageMapper extends BaseMapper<Country> {

    List<Country> getCountryByPage(RowBounds rowBounds);

    List<Country> CountryByPage();
}
