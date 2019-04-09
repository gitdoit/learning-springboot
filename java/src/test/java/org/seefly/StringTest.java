package org.seefly;

import java.lang.reflect.Field;

/**
 * @author liujianxin
 * @date 2018-10-26 09:40
 */
public class StringTest {

    public static void main (String[] args) {
        String str = "Mario";
        toLuigi(str);
        System.out.println(str + " " + "Mario");
    }

    public static void toLuigi(String original) {
        try {
            Field stringValue = String.class.getDeclaredField("value");
            stringValue.setAccessible(true);
            stringValue.set(original, "Luigi".toCharArray());
        } catch (Exception ex) {
            // Ignore exceptions
        }
    }


}
