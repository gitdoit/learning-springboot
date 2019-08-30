package org.seefly.lambda.stream;

import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
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


    @Test
    public void testAPI(){

        List<User> lis = new ArrayList<>();
        lis.add(null);
        // 不确定列表是否为null，可以用这个方法
        String fuck = Optional.ofNullable(lis)
                // 如果不为null，会执行这个逻辑，这里过滤为空列表的情况
                .filter(b -> b.size() > 0)
                // 如果上面被过滤掉了，这里也不会空指针。否则拿出第一个
                .map(li -> li.get(0))
                // 如果第一个元素为null,这里也不会空指针
                .map(User::getUserName)
                // 最后判断是否能拿出来东西，不行就返回默认的
                .orElse("fuck");
        System.out.println(fuck);
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
