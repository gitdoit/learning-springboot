package org.seefly.rocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Data
@Component
@ConfigurationProperties(prefix = "spring.rocketmq")
public class RocketProp {

    private String namesrvAddr;
    private String instanceName;
    private String clientIP;
    private ProducerConfig producer;
    private ConsumerConfig consumer;


    @Data
    public static class ProducerConfig{
        private String instanceName;
        private String tranInstanceName;
        private String group;
    }

    @Data
    public static class ConsumerConfig{
        private String instanceName;
        private Integer minConsumers;
        private Integer maxConsumers;

        public String getGroup(String applicationName){
            return applicationName == null ? "" : applicationName.toUpperCase();
        }
    }

}
