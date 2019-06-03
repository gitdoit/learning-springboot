package org.seefly.springweb.controller;

import org.seefly.springweb.annotation.MyParamAnno;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author liujianxin
 * @date 2018-07-26 10:32
 * 描述信息：本类用来演示参数接收的问题
 *
 * @RequestParam("name") 用来绑定请求路径中的参数
 *      注解中参数：required 指定路径中是否必须包含该参数，默认则是true。不包含则报错
 * @RequestBody 作用于参数上，用来指定该参数需要从请求体中获取数据进行绑定。
 *      注解中参数：required 默认为true，表示当请求体为空抛出异常
 * @PathVariable("id") 作用于参数上，绑定restful风格的参数
 *      注解中参数：required 默认为true，表示当请求路径中没有对应参数时抛出异常
 *
 * @RequestMapping
 *      consumes: 指定post请求时，请求体的类型
 *          consumes={"application/json","application/xml"} 表示只接受这两种类型的消息
 *          consumes="!text/plain" 除了这个格式其他的都接收
 *          consumes="application/*" 模糊匹配
 *
 *     method: 限定请求方法类型，例如get,post,delete,put...
 *
 *     params: 限定请求参数，参数为数据或字符串。
 *          例如{"!name","age","add=123"}表示参数中不能有name参数，必须有age参数，必须有add参数且值为123
 *
 *     headers: 根据语法匹配请求头中的参数，若不符合则不会处理
 *          例如{"content-type=text/*","aaa=sss"}，请求体的类型可以为text/*，且必须有aaa=sss。这里的请求体类型可以用下面的consumers限定
 *
 *     consumes: 限定请求体内容类型,即请求头中content-type字段的值（注意，get请求是没有请求体的，这个参数大都用在post请求），
 *          表明我这个接口只能处理指定的请求体类型，其他的不接受
 *          {"application/json","application/xml"},请求体消息类型必须时json或者xml的
 *
 *     produces: 响应体的消息类型，限制客户端请求头中的Accept字段值，如果类型对不上那么会报406
 *          此外Accept字段还决定了使用哪种消息转换器-HttpMessageConverter将消息写入响应体（前提是使用@ResponseBody）
 *          text/plain     --->  StringHttpMessageConverter
 *          application/json   --->  MappingJackson2HttpMessageConverter
 *
 *
 **/

@RestController
@RequestMapping
public class ParamController {
    @Value("${my.name}")
    private String name;
    @Value("${my.age}")
    private Integer age;


    @GetMapping("/aaa")
    public String test(){
        return "LK";
    }
    @GetMapping("/bbb")
    public String ssss(){
        int i = 1 / 0;
        return "LK";
    }

    @PostConstruct
    public void init(){
        System.out.println(name);
        System.out.println(age);
    }

    /**
     * 演示从路径中绑定restful参数
     * @param restful
     * @return
     */
    @GetMapping("/restful/{id}")
    public String restful(@PathVariable("id") String restful){
        System.out.println(restful);
        return restful;
    }


    /**
     * 演示从请求体中接收参数
     * @param json
     * @return
     */
    @PostMapping(value = "/post",consumes = "!text/plain")
    public String requestBody(@RequestBody String json, HttpServletRequest request){
        System.out.println(request.getContentType());
        System.out.println(json);
        return json;
    }

    /**
     * 使用参数限制请求：请求参数不能包含name,必须包含age,add,且add=123,请求方式为get,请求头中必须有字段
     */
    @RequestMapping(value = "/param",params = {"!name","age","add=123"},method=RequestMethod.GET,headers = {"test"})
    public String mappingParam(String age,String add,HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            System.out.println(headerName+":"+request.getHeader(headerName));

        }
        System.out.println(age);
        System.out.println(add);
        return age;
    }


    @RequestMapping(value = "/date",method = RequestMethod.GET)
    public String custmoPram(Date date){
        System.out.println(date);
        return date.toString();
    }

    /**
     * 测试自定义注解配合自定义消息解析器
     * 我自定义的消息解析器在检测到方法的参数上含有自定义注解@myParamAnno时会启用自定义
     * 消息转解析转换消息
     * @param name
     * @return
     */
    @RequestMapping(value = "/res",method = RequestMethod.GET)
    public String testCustom(@MyParamAnno String name){
        System.out.println(name);
        return name;
    }

    /**
     * 我们知道如果使用@RequestBody注解，那么将会启用RequestResponseBodyMethodProcessor
     * 进行消息处理，
     * @return
     */
    @RequestMapping(value = "/body")
    public String testRequestBody(@RequestBody String body){
        return null;
    }



    @PostMapping("/form")
    public String form(String name,String age){
        return "OK";
    }


}
