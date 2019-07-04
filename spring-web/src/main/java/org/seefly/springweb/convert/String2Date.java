package org.seefly.springweb.convert;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 这个Converter不能解析被@RequestBody标注的参数
 * 因为一旦被这个注解标注，那就使用Json来转了
 * @author liujianxin
 * @date 2018-07-06 00:00
 **/
@Component
public class String2Date implements Converter<String,Date> {


    @Override
    public Date convert(String source) {
        System.out.println("=================================================");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
