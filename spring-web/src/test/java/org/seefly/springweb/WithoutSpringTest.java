package org.seefly.springweb;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.seefly.springweb.controller.request.Request;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
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
    public void testDoJob() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
        List<String> strings = Files.readAllLines(Paths.get("E:\\a.txt"));
        for (String string : strings) {
            String[] line = string.split("\t");
            System.out.println(line[0]);
            System.out.println(line[1]);
    
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("code", "NDIR00"+line[0]);
            map.add("imei", line[1]);
            map.add("org", "611c61031e34321a9a43a862");
            map.add("tenant", "611c61031e34321a9a43a861");
    
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    
           
            String foo = restTemplate.postForObject("https://aiot.citysafety.com/v1/mini/manage/sensor",request,String.class);
            Boolean success = JSONObject.parseObject(foo).getBoolean("success");
            if(success) {
                System.out.println(string);
            }else {
                System.out.println(foo);
                break;
            }
        }
    }

    @Test
    public void testStream(){
        
        ObjectOutputStream objectOutputStream = null;
    }

}
