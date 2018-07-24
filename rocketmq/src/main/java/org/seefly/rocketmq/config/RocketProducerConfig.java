package org.seefly.rocketmq.config;

import lombok.extern.slf4j.Slf4j;
import me.robin.spring.rocketmq.TransactionCheckListenerImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.protocol.header.QueryMessageRequestHeader;
import org.apache.rocketmq.remoting.CommandCustomHeader;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class RocketProducerConfig {

    @Resource
    private RocketProp rocketConf;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfiles;

    //通信协议
    static final RPCHook RPC_HOOK = new RPCHook() {
        private final long reduceMills = 60000;
        @Override
        public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
            CommandCustomHeader customHeader = request.readCustomHeader();
            if (customHeader instanceof QueryMessageRequestHeader) {
                long beginTimestamp = ((QueryMessageRequestHeader) customHeader).getBeginTimestamp();
                if (beginTimestamp > reduceMills) {
                    ((QueryMessageRequestHeader) customHeader).setBeginTimestamp(beginTimestamp - reduceMills);
                }
            }
        }

        @Override
        public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {

        }
    };

    /**
     * 初始化向rocketmq下发指令的生产者
     */
    @Bean(destroyMethod = "shutdown")
    @Lazy
    @ConditionalOnMissingBean(value = MQProducer.class, name = "cmdProducer")
    public DefaultMQProducer cmdProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(rocketConf.getConsumer().getGroup(applicationName) + "_" + activeProfiles, RPC_HOOK);
        producer.setNamesrvAddr(rocketConf.getNamesrvAddr());
        if (StringUtils.isNotBlank(rocketConf.getProducer().getInstanceName())) {
            producer.setInstanceName(rocketConf.getProducer().getInstanceName());
        }
        producer.setVipChannelEnabled(false);
        producer.start();
        log.info("RocketMq cmdProducer Started.");
        return producer;
    }

    @Bean(name = "transactionMQProducer", destroyMethod = "shutdown")
    @Lazy
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        /*
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        TransactionMQProducer producer = new TransactionMQProducer("TMQP-" + rocketConf.getConsumer().getGroup(applicationName) + "_" + activeProfiles, RPC_HOOK);
        producer.setNamesrvAddr(rocketConf.getNamesrvAddr());
        if (StringUtils.isNotBlank(rocketConf.getProducer().getTranInstanceName())) {
            producer.setInstanceName(rocketConf.getProducer().getTranInstanceName());
        }
        if (StringUtils.isNotBlank(rocketConf.getProducer().getGroup())) {
            producer.setProducerGroup(rocketConf.getProducer().getGroup());
        }
        // 事务回查最小并发数
        producer.setCheckThreadPoolMinSize(2);
        // 事务回查最大并发数
        producer.setCheckThreadPoolMaxSize(2);
        // 队列数
        producer.setCheckRequestHoldMax(2000);

        // 由于社区版本的服务器阉割调了消息回查的功能，所以这个地方没有意义
        TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
        producer.setTransactionCheckListener(transactionCheckListener);

        /*
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();
        log.info("RocketMq TransactionMQProducer Started.");
        return producer;
    }
}