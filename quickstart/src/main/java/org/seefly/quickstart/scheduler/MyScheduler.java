package org.seefly.quickstart.scheduler;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liujianxin
 * @date 2018-08-03 14:44
 * 描述信息：
 **/
@Component
public class MyScheduler {

    @Resource(name="taskExecutor")
    private TaskScheduler taskSchedule;

    @Scheduled(fixedDelay = 2000)
    public void sayHello(){
        System.out.println("当前任务线程名："+Thread.currentThread().getName());
        System.out.println("当前任务优先级"+Thread.currentThread().getPriority());
        System.out.println("当前线程线程组"+Thread.currentThread().getThreadGroup().getName());
    }

    @Scheduled(fixedDelay = 5000)
    public void checkExecutor(){
        if (taskSchedule instanceof ThreadPoolTaskScheduler){
            System.out.println("=====当前线程池活动的线程"+((ThreadPoolTaskScheduler) taskSchedule).getActiveCount());
            System.out.println("=====当前线程池容量"+((ThreadPoolTaskScheduler) taskSchedule).getPoolSize());
        }
    }
}
