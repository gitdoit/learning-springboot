package org.seefly.springannotation.lifecycle.hook;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ShutdownHookRegistry {

    private static ShutdownHookRegistry shutdownHookRegistry;

    private List<ShutdownHook> shutdownHooks;

    private ShutdownHookRegistry() {

        shutdownHooks = new ArrayList<>();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("start shutdown thread...");
            ExecutorService executor = Executors.newFixedThreadPool(10);
            shutdownHooks.forEach(hook -> {
                try{
                    executor.submit(hook::shutdown);
                }catch(Exception e){
                    log.error("ShutdownHookRegistry run error");
                }
            });

            try {
                Thread.sleep(2000);
                executor.shutdown();
            } catch (Exception e) {
            }
        }
        ));
    }


    public static ShutdownHookRegistry getInstance() {
        if (null == shutdownHookRegistry) {
            shutdownHookRegistry = new ShutdownHookRegistry();
        }

        return shutdownHookRegistry;
    }

    public void addHooks(ShutdownHook hook){
        shutdownHooks.add(hook);
    }


}
