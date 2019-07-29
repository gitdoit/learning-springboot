package org.seefly.actuator.schedul;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 牛逼牛逼牛逼，spring-actuator可以动态的修改日志等级
 * post ->          http://localhost/actuator/loggers/root
 * body ->          { "configuredLevel": "DEBUG" }
 * content-type->   application/json
 *
 *
 *
 * http://localhost/actuator/loggers/root 这种方式是全局的修改日志等级
 * http://localhost/actuator/loggers/org.seefly.actuator.schedul.ActuatorSchedul 这种是针对某个类具体的定制日志等级
 * 可以get -> http://localhost/actuator/loggers 查看所有可操作的日志类
 *
 *
 * @author liujianxin
 * @date 2019-07-29 13:25
 */
@Slf4j
@Component
public class ActuatorSchedul {

    /**
     * 用来展示动态的修改日志等级
     * 也可以访问 http://localhost/actuator/scheduledtasks
     * 来查看应用中的计划任务
     */
    @Scheduled(fixedRate = 1000)
    public void showLog(){
      log.debug("debug");
      log.info("info");
      log.warn("worn");
    }
}
