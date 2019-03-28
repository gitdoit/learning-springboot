package org.seefly.time;

import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 一个时钟，使用时区提供对当前时刻，日期和时间的访问。
 * 此类的实例用于查找当前时刻，可以使用存储的时区来解释当前时刻以查找当前日期和时间。
 * 因此，可以使用时钟代替System.currentTimeMillis（）和TimeZone.getDefault（）
 * @author liujianxin
 * @date 2019-01-24 20:58
 */
public class ClockDemo {

    /**
     * 1548334757867
     * 1548334758868
     * 1548334759869
     * 1548334760870
     * 1548334761870
     * 1548334762871
     * 1548334763872
     * 1548334764872
     */
    @Test
    public void test() throws InterruptedException {
        Clock clock = Clock.systemDefaultZone();
        while (true){
            // 输出是会变的
            System.out.println(clock.millis());
            Thread.sleep(1000);
        }
    }

    @Test
    public void testUseInApplication(){
        Clock clock = Clock.systemDefaultZone();
        LocalDate fromOutSide = LocalDate.of(2018,11,11);
        System.out.println(fromOutSide.isBefore(LocalDate.now(clock)));

    }

}
