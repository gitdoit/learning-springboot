package org.seefly.springannotation.lifecycle;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * refresh
 *  ....
 *  初始化剩余单例
 *  -->finishRefresh
 *      ....
 *      --> DefaultLifecycleProcessor.onRefresh()
 *          ---> 根据阶段配置,调用start方法
 *      ....
 * @author liujianxin
 * @date 2021/4/14 11:27
 */
@Component
public class BySmartLifecycle implements SmartLifecycle {
    private boolean isRunning = false;
    
    @Override
    public void start() {
        System.out.println("[BySmartLifecycle]的start()方法被调用");
        this.isRunning = true;
    }
    
    @Override
    public void stop() {
        System.out.println("[BySmartLifecycle]的stop()方法被调用");
    }
    
    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
