package org.seefly.rocketmq;

import org.junit.Test;
import org.seefly.rocketmq.model.dto.ModifyUserDto;
import org.seefly.rocketmq.model.dto.ResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author liujianxin
 * @date 2018-07-16 13:28
 * 描述信息：测试reustFul风格接口
 **/

public class RestTest {
    private RestTemplate rest = new RestTemplate();

    /**
     * 使用getForObject方法请求restFul风格接口
     * 此接口会根据ResDto.class通过反射封装响应信息（无头信息）
     */
    @Test
    public void testGetByObject(){
        ResDto resDto = rest.getForObject("http://127.0.0.1:8080/user/12", ResDto.class);
        System.out.println(resDto);
    }

    /**
     * 使用 getForEntity访问restFul风格接口
     * 此方法和getForObject方法很类似，但是这个方法会额外的的获取响应头信息
     */
    @Test
    public void testGetByEntity(){
        ResponseEntity<ResDto> res = rest.getForEntity("http://127.0.0.1:8080/user/12", ResDto.class);
        System.out.println(res);
    }

    @Test
    public void testPut(){
        ModifyUserDto user = new ModifyUserDto();
        user.setId("12");
        user.setName("abc");
        //所有版本的put方法中，第二个参数都是需要发送的资源
        //如果是String类型，那么会使用StringHttpMessageConverter转换,Content-type:text/plain
        //如果是java类，那么会使用MappingJacksonHttpMessageConverter,Content-type:application/json
        //如果是MultyValueMap<String,String>,那么会使用FormatHttpMessageConverter,Content-type:application/x-www-form-formurlencoded
        rest.put("http://127.0.0.1:8080/user",user);
    }

    @Test
    public void testPutRUI(){
        ModifyUserDto user = new ModifyUserDto();
        user.setId("12");
        user.setName("abc");
        rest.put(URI.create("http://127.0.0.1:8080/user"),user);
    }

    /**
     * 有三个参数，前两个都和前面的一样，最后一个是可变长参数
     * 它对应URL中的对应位置的占位符
     */
    @Test
    public void testPutParam(){
        ModifyUserDto user = new ModifyUserDto();
        user.setId("12");
        user.setName("abc");
        rest.put("http://127.0.0.1:8080/user/{id}/{haha}",user,12,"sdfs");
    }
}
