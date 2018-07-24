package org.seefly.stream;

import lombok.Data;
import org.junit.Test;

import java.util.Optional;

/**
 * @author liujianxin
 * @date 2018-07-08 16:29
 * 描述信息：
 **/
public class OptionalTest {

    /**
     * Optional有三种构造方式
     */
    @Test
    public void testStart(){
        //构造一个空的Optional
        Optional<Object> way1 = Optional.empty();
        //根据给定参数构造一个Optional,参数不能为空，为空抛异常
        Optional<String> way2 = Optional.of("");
        //根据指定参数构造一个Optional,参数可空
        Optional<Object> way3 = Optional.ofNullable(null);


        User user = new User("user","seefly@vip.qq.com");
        Optional<Object> o = Optional.ofNullable(user);
        //基本用法。如果存在，则执行给定的lambda
        o.ifPresent(System.out::print);
    }

    @Data
    private static class User{
        private String userName;
        private String email;
        public User(String userName,String email){
            this.email = email;
            this.userName = userName;
        }
    }
}
