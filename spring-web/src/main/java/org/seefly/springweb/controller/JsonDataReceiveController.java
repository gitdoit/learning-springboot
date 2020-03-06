package org.seefly.springweb.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 演示post请求之Content-type=application/json时的参数接收
 *
 * @author liujianxin
 * @date 2020/3/5 16:03
 */
@RestController
@RequestMapping("json")
public class JsonDataReceiveController {

    /**
     * 使用字符串接收json数据
     * 直接用一个字符串来接收json串也是没有问题的
     */
    @PostMapping(value = "/string",consumes = "!text/plain")
    public String requestBody(@RequestBody String json, HttpServletRequest request){
        System.out.println(request.getContentType());
        System.out.println(json);
        return json;
    }

    /**
     * 接收数组数据
     * 前端
     *      url: /json/array?item=1,2,3 (也可以像get请求一样，在uri里面追加参数。神奇呢)
     *      Content-Type:application/json
     *      requestBody: ['aa','bb','cc']
     *
     */
    @PostMapping("/array")
    public String[] postArray(String[] item,@RequestBody String[] arr){
        System.out.println(item);
        return item;
    }

    /**
     * {
     * 	"string":"字符串",
     * 	"integer":121,
     * 	"aDouble":"12.1",
     * 	"decimal":121.11,
     * 	"array":["a,b,c"],
     * 	"list":["a","b","c"],
     * 	"date":"2020-02-22 11:11:11",
     * 	"map":{"k1":"v1","k2":"v2"}
     * }
     */
    @PostMapping("/bean")
    public String bean(@RequestBody Optional<JsonData> data){
        JsonData jsonData = data.get();
        System.out.println(jsonData);
        return "OK";
    }


    @Data
    public static class JsonData{
        private String string;
        private Integer integer;
        // 不行？？
        private double aDouble;
        private BigDecimal decimal;
        private String[] array;
        private ArrayList<String> list;
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
        private Date date;
        private Map<String,String> map;

    }
}
