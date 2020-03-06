package org.seefly.springweb.controller;

import lombok.Data;
import org.seefly.springweb.convert.String2UserConverter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 演示前端Post请求传送表单参数时后台的各种接收姿势
 * 即Content-type=multipart/form-data的请求
 * 这种请求可以上传文件
 *
 * 参数绑定处处理：
 * @author liujianxin
 * @date 2020/3/5 15:46
 */
@RestController
@RequestMapping("form-data")
public class FormDataReceiveController {

    /**
     * 接收表单参数之日期类型
     * 演示前端传入字符串：2019-02-03
     * 在这里直接使用日期类型接收，而不用自定义转换器。
     * 若自定义了一个{@link String2UserConverter}，然后还要用这个注解的话 这个注解是不会起作用的，起作用的是上面那个。
     */
    @PostMapping("/format-date")
    public String format(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf.format(date));
        return "OK";
    }

    /**
     * ModelAttribute的作用就是从表单中获取指定的参数绑定到当前属性上，没有特别的
     * 就是相当于一个别名；
     * {@link ModelAttributeMethodProcessor}
     * 可在 {@link ModelFactory#getNameForParameter(org.springframework.core.MethodParameter)}看到逻辑
     */
    @PostMapping("/model-attr")
    public String modelAttr(@ModelAttribute("black") String white){
        System.out.println(white);
        return "OK";
    }

    /**
     * 接收表单参数之格式化数字类型
     * 用NumberFormat转换数字类型
     * 像是转换金额: money = 1,234,456.89
     */
    @PostMapping("/format-number")
    public String numberFormat(@NumberFormat(pattern = "#,###.##") Double money){
        System.out.println(money);
        return "OK";
    }

    /**
     * 接受表单参数之bean的Fields格式化
     *
     * 对于application/form-data参数中的日期类型接收格式化可以用：@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     * 对于application/json且使用的是Jackson，可以用 @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
     *
     */
    @PostMapping("/java-bean")
    public String formatDto(FormDataRequest request){
        System.out.println(request);
        return "OK";
    }

    /**
     * 接收form列表，不能和jsonn那样直接用数组接收；
     *
     * requests[0].string:字符串
     * requests[0].integer:1211
     * requests[0].array:数,组,接,收
     * requests[0].date:2020-02-02 11:11:11
     * requests[0].list:1,2,3,4
     * requests[0].aDouble:1122.12
     * requests[0].formatDouble:1,223,22
     * requests[0].decimal:322.32
     */
    @PostMapping("/form-list-bean")
    public String formList(ListRequest requests){
        System.out.println(requests);
        return "OK";
    }

    @Data
    public static class ListRequest{
        List<FormDataRequest> requests;
    }

    @Data
    public static class FormDataRequest{
        private String string;
        private Integer integer;
        private Double aDouble;
        @NumberFormat(pattern = "#,###.##")
        private Double formatDouble;
        private BigDecimal decimal;
        private String[] array;
        private ArrayList<String> list;
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date date;
        // map类型需要写转换器String2MapConverter
    }

}
