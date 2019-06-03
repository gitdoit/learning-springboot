package org.seefly.springweb01.log;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.seefly.springweb01.log.event.AuditEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

/**
 * 多线程异常捕获
 * controller异常捕获
 * 审计日志aop
 * @author liujianxin
 * @date 2019-04-25 09:35
 */

@Import({AuditHandler.class,GlobalExceptionHandler.class})
public class LogService implements InitializingBean {
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${logservice.rocketmq.namesrv-addr}")
    private String logServiceNamesrvAddr;
    private DefaultMQProducer producer;



    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("++++++++++++++++++++++++++++++");
        DefaultMQProducer producer =  new DefaultMQProducer("log_service_producer_group");
        producer.setInstanceName("log_service");
        producer.setNamesrvAddr(logServiceNamesrvAddr);
        producer.setRetryAnotherBrokerWhenNotStoreOK(false);
        producer.start();
        this.producer = producer;
    }


    @EventListener()
    public void auditEventListener(AuditEvent auditEvent){
        // topic
        // tag
        // applicationName
        System.out.println(auditEvent);
    }
}
