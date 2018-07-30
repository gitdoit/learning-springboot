package org.seefly.rocketmq.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.robin.spring.rocketmq.ConsumerConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.seefly.rocketmq.listener.MessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@Slf4j
public class RocketConsumerConfig {

    @Resource
    private RocketProp rocketConf;

    @Value("${spring.profiles:pro}")
    private String env;

    @Value("${spring.rocketmq.topic.event-topic}")
    private String eventTopic;
    /**
     * spring事件发布
     */
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 群发消息结果通知的消费者
     */
    @Bean(destroyMethod = "shutdown", name = "myConsumer")
    @Lazy(false)
    public MQPushConsumer wxMsgTaskResultConsumer() throws MQClientException {
        // consumerGroup,协议，消息队列算法-平均散列队列算法
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(env + "_consumer_group_cmd", RocketProducerConfig.RPC_HOOK, new AllocateMessageQueueAveragely());
        //指定NameServer地址
        consumer.setNamesrvAddr(rocketConf.getNamesrvAddr());
        //
        //consumer.setMessageModel(rocketConf.getConsumer().getMessageModel());

        //设置消费者实例名称
        if (StringUtils.isNotBlank(rocketConf.getConsumer().getInstanceName())) {
            consumer.setInstanceName(rocketConf.getConsumer().getInstanceName());
        }

        ConsumerConfig consumerConfig = rocketConf.getConsumer();
        //consumer.setConsumeMessageBatchMaxSize(5);//设置批量消费，以提升消费吞吐量，默认是1
        consumer.setConsumeThreadMin(consumerConfig.getMinConsumers());
        consumer.setConsumeThreadMax(consumerConfig.getMaxConsumers());
        //设置订阅的topic
        consumer.subscribe(eventTopic, "*");
        //注册消息监听，给一个匿名的Event
        consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            MessageExt msg = msgs.get(0);
            try {
                System.out.println("有消息");
                //自定义事件
                MessageListener event = JSON.parseObject(new String(msg.getBody()), MessageListener.class);
                event.setId(msg.getMsgId());
                //配合spring的事件机制，当有消息时则发布一个事件，对应的监听器则会监听到
                eventPublisher.publishEvent(event);
            } catch (Exception e) {
                log.error("消息处理异常 [{}:{}]", msg.getTopic(), msg.getMsgId(), e);
                //重复消费3次
                if (msg.getReconsumeTimes() >= 3) {
                    log.warn("该消息重复消费（失败）超过3次【{}】", msg.getMsgId());
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            //如果没有return success，consumer会重复消费此信息，直到success
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("cmdConsumer started....");
        return consumer;

    }
}
