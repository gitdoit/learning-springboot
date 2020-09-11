package org.seefly.springweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.junit.Test;
import org.seefly.springweb.controller.JsonDataReceiveController;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author liujianxin
 * @date 2020/8/19 11:05
 */
public class JacksonTest {

    @Test
    public void testSer() throws JsonProcessingException {
        ObjectMapper build = new Jackson2ObjectMapperBuilder()
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .timeZone(TimeZone.getTimeZone("GMT+8"))
                .build();
        JsonDataReceiveController.TimeData date = new JsonDataReceiveController.TimeData();
        date.setDateTime(LocalDateTime.now());

        System.out.println(build.writeValueAsString(date));

    }
}
