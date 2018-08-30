package org.seefly.quickstart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.seefly.quickstart.model.MacInfo;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-08-09 11:27
 * 描述信息：
 **/
public interface MacMapper extends BaseMapper<MacInfo> {

    List<MacInfo> selectByNum(@Param("start") int start, @Param("end") int end);
}
