package org.seefly.juc.future.jdk;

import java.util.concurrent.Callable;

/**
 * @author liujianxin
 * @date 2018-11-22 14:13
 */
public class JdkRealData implements Callable<String> {
    private String para;

    public JdkRealData(String para) {
        this.para = para;
    }

    @Override
    public String call() throws Exception {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 10;i++){
            sb.append(para);
            Thread.sleep(100);
        }
        return sb.toString();
    }
}
