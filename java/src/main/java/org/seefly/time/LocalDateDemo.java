package org.seefly.time;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * LocalDate代表的是 2019-01-24 形式的日期
 * @author liujianxin
 * @date 2019-01-24 20:16
 */
public class LocalDateDemo {


    /**
     * 初始化方式
     */
    @Test
    public void test(){
        LocalDate of = LocalDate.of(2019, 1, 12);
        LocalDate of1 = LocalDate.of(2018, Month.APRIL, 11);
        LocalDate localDate = LocalDate.now();

        LocalTime localTime = LocalTime.of(20, 13);

        LocalDateTime of2 = LocalDateTime.of(2019, 1, 24, 20, 33, 22);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
    }

    /**
     * 字符串转日期
     */
    @Test
    public void testGetLocalDateFromString(){
        String str = "2019-01-24";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parse = LocalDate.parse(str, dateTimeFormatter);
        System.out.println(parse);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * 时间戳互转
     */
    @Test
    public void testTimeS(){
        // 时间戳传LocalDateTime
        long timestamp = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // LocalTime转时间戳
        LocalDateTime dateTime = LocalDateTime.now();
        long l = dateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long l1 = dateTime.toInstant(ZoneOffset.of("+08:00")).toEpochMilli();
        long l2 = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Date互转
     */
    @Test
    public void test2Date(){
        // Date转LocalDateTime
        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 日期加减操作
     */
    @Test
    public void testOp(){
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(1);
        LocalDate localDate1 = now.plusMonths(1);
        LocalDate localDate2 = now.plusWeeks(1);
        LocalDate localDate3 = now.plusYears(1);

        LocalTime now1 = LocalTime.now();
        LocalTime localTime = now1.plusHours(1);

    }
}
