package org.seefly.rocketmq.config;

import lombok.Data;
import me.robin.spring.rocketmq.ConsumerConfig;
import me.robin.spring.rocketmq.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.rocketmq")
public class RocketProp {

    private String namesrvAddr;
    private String instanceName;
    private String clientIP;
    private ProducerConfig producer;
    private ConsumerConfig consumer;

}
