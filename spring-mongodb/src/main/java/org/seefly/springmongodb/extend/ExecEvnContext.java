package org.seefly.springmongodb.extend;

/**
 * @author liujianxin
 * @date 2021/9/8 16:32
 */
public class ExecEvnContext {
    private static final ThreadLocal<Boolean> ctx = new ThreadLocal<>();
    static {
        ctx.set(false);
    }

    public static void asTenant() {
        ctx.set(true);
    }

    public static void clear() {
        ctx.set(false);
    }

    public static boolean get() {
        return ctx.get();
    }
}
