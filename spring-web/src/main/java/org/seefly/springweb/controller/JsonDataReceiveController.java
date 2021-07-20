package org.seefly.springweb.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.seefly.springweb.enums.GenderEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    /**
     * 演示Jackson 对LocalDateTime的序列化和反序列化
     * 在springboot 2.x 里面 直接使用 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     * 对请求体的Date、LocalDateTime参数序列化，和返回值的反序列化都是可以的
     *
     * 那么如果我们直接使用ObjectMapper对json字符串手动序列化，应该怎么弄呢？
     *
     * 1、对于手动序列化，我们需要在字段上添加两个注解
     *   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     *   @JsonSerialize(using = LocalDateTimeSerializer.class)
     * 2、对于手动反序列化，我们需要在字段上添加两个注解
     *   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     *   @JsonDeserialize(using = LocalDateTimeDeserializer.class)
     *
     * 总结一下
     *    对于请求参数而言，只需要标注JsonFormat注解, 那么Spring框架就会自动帮我们序列化好请求参数了(当然需要使用Jackson)
     *    对于手动序列化和反序列化除了上面的JsonFormat注解还要额外的添加JsonSerialize和JsonDeserialize
     *
     * 对于全局的ObjectMapper配置 详见 org.seefly.springweb.JacksonTest#testSer()
     * 这样省得在每个LocalDateTime上添加这么多注解了,同时对于需要定制化的格式，我们只需要在这个
     * 属性上使用注解就行了，否则使用全局的默认配置
     *
     * @see <a href="https://stackoverflow.com/questions/28802544/java-8-localdate-jackson-format/53251526#53251526">jackson format<a/>
     */
    @PostMapping("/data-time")
    public TimeData serBean(@RequestBody TimeData data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        System.out.println("手动序列化:"+json);
        TimeData timeData = mapper.readValue(json, TimeData.class);
        System.out.println("手动反序列化:"+timeData);
        return data;
    }


    /**
     * 一个比较常见的需求
     * 前端传入异常枚举value，例如1、2、3这样的，后端需要序列化为对应的枚举实例;
     * 只要在枚举的反序列化依赖的字段上添加 @JsonValue 注解就行
     * <a href="https://stackoverflow.com/questions/12468764/jackson-enum-serializing-and-deserializer">
     *     jackson-enum-serializing-and-deserializer
     * <a/>
     */
    @PostMapping("/deser-enum")
    public String deserializeEnum(@RequestBody Person person){
        System.out.println(person);
        return person.gender.name();
    }




    /***************************************************************************************************************************/

    @Data
    public static class Person{
        private String name;
        private GenderEnum gender;
    }

    @Data
    public static class TimeData{
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime dateTime;
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
