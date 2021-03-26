package org.seefly.springweb;

import org.junit.Test;
import org.seefly.springweb.controller.request.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2019-06-14 20:25
 */
public class WithoutSpringTest {

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        Request request = new Request();
        request.setString("123");
        Class<?> clazz = Request.class;
        Field string = clazz.getDeclaredField("string");
        string.setAccessible(true);
        Object o = string.get(request);
        System.out.println(o);
    }

    @Test
    public void testStream(){
        
        ObjectOutputStream objectOutputStream = null;
    }

}
