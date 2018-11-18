package org.seefly.quickstart.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.*;

/**
 * @author liujianxin
 * @date 2018-06-16 22:04
 * 描述信息：spring推荐使用代码配置的方式代替xml
 * @Configuration声明这是一个配置类
 * @Bean代替xmlz中的<bean><bean/>标签，作用在方法上 方法名就是这个bean的id，返回值就是需要向容器中注入的组件，需要手动new
 * 这些注解都是spring的东西
 * @EnableAsync 开启异步执行，使@Async注解生效
 * @EnableScheduling 开启任务定时任务调度，使@Scheduled(cron = "20 34 17 * * ?") 注解生效
 **/
@EnableAsync
@EnableScheduling
@Configuration
public class ThreadConfig {


    /**
     * 配置异步执行处理器
     * 配置了这个东西之后只要在需要异步执行的方法或者类上使用@Async注解之后
     * 这个类或者方法就是异步执行了，很方便
     */
    @Bean
    public AsyncConfigurer asyncConfigurer() {
        AsyncConfigurer asyncConfigurer = new AsyncConfigurer() {
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
             * 异步未捕获异常处理
             * @return
             */
            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return (throwable, method, objects) -> {
                    System.out.println(throwable.getMessage() + "捕获到了");
                    System.out.println(method.getName() + "抛了异常");
                    System.out.println(objects);
                };
            }
        };

        return asyncConfigurer;
    }


    /**
     * 自定义定时任务调度器
     * 之前使用定时任务功能是在某个方法上加上@Scheduled(fixedDelay = 5000)注解，并使用@EnableAsync开启定时任务功能
     * 但是这样做有一些弊端就是，有多个定时任务同时执行时默认只会使用同一个线程来处理，所以他们就是串行的了
     * 现在我们通过实现SchedulingConfigurer类来自定义任务调度器，以更加细粒度的方式处理任务调度
     */
    @Bean
    public SchedulingConfigurer schedulingConfigurer() {
        return (taskRegistrar) -> {
            //设置任务执行器，替换默认的单线程执行器
            taskRegistrar.setScheduler(taskExecutor());
        };
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
