package org.seefly.springannotation.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.seefly.springannotation.lifecycle.hook.ShutdownHook;
import org.seefly.springannotation.lifecycle.hook.ShutdownHookRegistry;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author liujianxin
 * @date 2021/8/10 16:00
 */
@Slf4j
@Component
public class JvmShutdownHook implements ShutdownHook {

    @PostConstruct
    public void init() {
        ShutdownHookRegistry.getInstance().addHooks(this);
    }

    @Override
    public void shutdown() {
        log.info("[JvmShutdownHook] 被关闭");
    }
}
