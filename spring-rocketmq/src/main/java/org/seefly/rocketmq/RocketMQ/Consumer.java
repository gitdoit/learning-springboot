package org.seefly.rocketmq.RocketMQ;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

/**
 * @author liujianxin
 * @date 2018-07-12 10:33
 * 描述信息：
 **/
public class Consumer {


    public static void main(String[] args) throws InterruptedException, MQClientException {

        //声明并初始化一个consumer
        //需要一个consumer group名字作为构造方法的参数，这里为consumer1
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer1");

        //同样也要设置NameServer地址
        consumer.setNamesrvAddr("119.23.252.178:9876");

        //这里设置的是一个consumer的消费策略
        //CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
        //CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
        //CONSUME_FROM_TIMESTAMP 从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //设置consumer所订阅的Topic和Tag，*代表全部的Tag
        consumer.subscribe("TopicTest", "*");

        //设置一个Listener，主要进行消息的逻辑处理
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, contest) ->{
            System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
            //返回消费状态
            //CONSUME_SUCCESS 消费成功
            //RECONSUME_LATER 消费失败，需要稍后重新消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //调用start()方法启动consumer
        consumer.start();

        System.out.println("Consumer Started.");
    }

}
