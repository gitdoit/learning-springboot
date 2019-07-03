package org.seefly.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author liujianxin
 * @date 2019-07-01 17:17
 */
public class CalendarDemo {

    @Test
    public void testAPI(){
        // 返回一个当前时区，一个当前时间的Calendar实例
        Calendar instance = Calendar.getInstance();
        // 加一天，超出当月那就变成下个月1号
        instance.add(Calendar.DAY_OF_MONTH,1);
        // 滚动的加一天，如果超出当月，那就置为当月1号。
        instance.roll(Calendar.DAY_OF_MONTH,1);

        // 传入指定日期，然后就可以在指定日期上进行操作
        instance.setTime(new Date());

        // 把当前实例设置为当月的22号
        instance.set(Calendar.DAY_OF_MONTH,22);

        // 获取当前日期 在当月中的那一天
        int i = instance.get(Calendar.DAY_OF_MONTH);

    }

    @Test
    public void testAfter(){
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH,2);
        // 今天1号，明天2号。今天不在明天之后。返回false
        System.out.println(Calendar.getInstance().after(instance));
    }
}
