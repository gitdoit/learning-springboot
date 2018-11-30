package org.seefly.springbasic.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.*;

/**
 * spring推荐使用代码配置的方式代替xml
 *
 * 这些注解都是spring的东西
 * {@link EnableAsync}开启异步执行，使@Async注解生效
 * {@link EnableScheduling} 开启任务定时任务调度，使@Scheduled(cron = "20 34 17 * * ?") 注解生效
 * @author liujianxin
 * @date 2018-06-16 22:04
 **/
@EnableAsync
@EnableScheduling
@Configuration
public class ThreadConfig implements AsyncConfigurer,SchedulingConfigurer{

    /**
     * 默认情况下，Spring将搜索关联的线程池定义：上下文中的唯一org.springframework.core.task.TaskExecutor bean，
     * 否则为名为“taskExecutor”的java.util.concurrent.Executor bean。
     * 如果两者都不可解析，则将使用org.springframework.core.task.SimpleAsyncTaskExecutor来处理异步方法调用。
     * 此外，具有void返回类型的带注释的方法不能将任何异常发送回调用者。默认情况下，仅记录此类未捕获的异常。
     * 要自定义所有这些，请实现AsyncConfigurer并通过getAsyncExecutor（）方法提供：您自己的Executor
     * 并通过getAsyncUncaughtExceptionHandler（）方法提供您自己的AsyncUncaughtExceptionHandler
     */
    @Override
    public Executor getAsyncExecutor() {
        //这种线程池无法计划执行任务
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //ThreadPoo
        threadPool.setDaemon(false);
        //设置核心线程数
        threadPool.setCorePoolSize(10);
        //设置最大线程数
        threadPool.setMaxPoolSize(20);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(50);
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);
        //  线程名称前缀
        threadPool.setThreadNamePrefix("MyAsync-");
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }

    /**
     * AsyncConfigurer的接口方法
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            System.out.println(throwable.getMessage() + "捕获到了");
            System.out.println(method.getName() + "抛了异常");
            System.out.println(objects);
        };
    }






    /**
     *
     * 若使用了{@link EnableScheduling}注解,spring会自动生成{@link ScheduledAnnotationBeanPostProcessor}实例，并在初始化
     * 完成之后执行{@link ScheduledAnnotationBeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)}
     * 可以看到它会扫描所有组件上的所有方法，若有{@link Schedules}注解，都会被注册进来。然后使用默认的{@link ScheduledTaskRegistrar}来执行
     * 在{@link ScheduledTaskRegistrar#afterPropertiesSet()}这里可以看到，它使用的是单线程的线程池。
     * 我们实现{@link SchedulingConfigurer}，并向容器中注入该组件，它会在{@link ScheduledAnnotationBeanPostProcessor#onApplicationEvent(org.springframework.context.event.ContextRefreshedEvent)}
     * 处替换掉默认的。
     *
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //设置任务执行器，替换默认的单线程执行器
        taskRegistrar.setScheduler(taskExecutor());
    }


    /**
     * 1. ThreadPoolExecutor 该类为java.util.concurrent包下
     * 参数说明：
     * corePoolSize:核心线程池大小
     * maximumPoolSize:最大线程数,具有以下几种情形
     * 当没有指定线程队列容量时：
     * 1.当前线程数 < corePoolSize,直接创建新线程执行任务
     * 2.当前线程数 > corePoolSize 并且 queue = LinkedBlockingDeque
     * 任务被放入队列中排队等待执行,maximumPoolSize在此种情形下无效
     * 3.当前线程数 between corePoolSize and maximumPoolSize 并且queue = SynchronousQueue
     * 直接创建新线程执行任务，并在执行完毕超时后终止该线程
     * 4.当前线程数 > corePoolSize and 当前线程数 > maximumPoolSize 并且queue = SynchronousQueue
     * 拒绝接收新任务，并抛出异常。
     * <p>
     * 当指定线程队列容量时：
     * 1.LinkedBlockingDeque塞满时
     * 直接开启新线程执行任务，当线程数 > maximumPoolSize 抛出异常
     * 2.SynchronousQueue没有队列容量，任何任务进来都是直接执行，当线程数 > maximumPoolSize 抛出异常
     * <p>
     * keepAliveTime:线程闲置时间阈值，线程在闲置超过指定时间后将会被终止
     * 该参数默认只对非核心线程起作用，可以使用allowCoreThreadTimeOut()方法
     * 设置对核心线程也会启用超时终止。
     * Queue:线程队列
     * ThreadFactory:线程工厂
     * ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 10, 500,
     * TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(),factory);
     * <p>
     * <p>
     * 2. ScheduledThreadPoolExecutor
     * 该类为jdk中的线程池，包装了底层的ThreadPoolExecutor，用来执行一些周期定时任务。
     * <p>
     * 3.ThreadPoolTaskScheduler
     * 该类为spring的线程池，里面包装使用了ScheduleThreadPoolExecutor
     * 用来执行一些周期定时任务，更方便定制功能。
     * <p>
     * 4.ThreadPoolTaskExecutor
     * 该类为spring的线程池，底层包装了jdk的ThreadPoolExecutor,用来执行一些
     * 异步任务
     * <p>
     * CustomizableThreadFactory
     * 该类为spring提供，实现了jdk中的ThreadFactory接口，用来创建线程
     * 可以自定义被创建线程的一些属性例如优先级，线程名等
     * <p>
     * <p>
     * <p>
     * 自定义任务执行器，被任务调度器调用
     */
    @Bean
    public TaskScheduler taskExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadPriority(Thread.MAX_PRIORITY);
        threadPoolTaskScheduler.setThreadNamePrefix("Scheduler-");
        //设置再任务被拒绝执行时的处理器
        threadPoolTaskScheduler.setRejectedExecutionHandler(null);
        threadPoolTaskScheduler.setPoolSize(20);
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        threadPoolTaskScheduler.setErrorHandler((t) -> System.out.println(t.getMessage()));
        threadPoolTaskScheduler.setDaemon(false);
        return threadPoolTaskScheduler;
    }


}
