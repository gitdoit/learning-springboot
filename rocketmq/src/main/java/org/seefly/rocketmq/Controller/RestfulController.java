package org.seefly.rocketmq.Controller;

import org.seefly.rocketmq.model.dto.ModifyUserDto;
import org.seefly.rocketmq.model.dto.ResDto;
import org.springframework.web.bind.annotation.*;

/**
 * @author liujianxin
 * @date 2018-07-16 14:13
 * 描述信息：学习restful风格
 **/
@RestController
public class RestfulController {

    /**
     * 模拟获取用户信息：get user  {id}
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public ResDto getInfo(@PathVariable("id") String id){
        ResDto dto = new ResDto();
        dto.setCode(Integer.valueOf(id));
        dto.setMessage("OK");
        dto.setSuccess(true);
        return dto;
    }

    /**
     * 使用put请求修改客户信息
     * @param dto
     */
    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    public void modifyUserInfo(@RequestBody ModifyUserDto dto){
        System.out.println("修改客户信息："+dto);
    }
}
