package org.seefly.rocketmq.Controller;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liujianxin
 * @date 2018-07-12 16:59
 * 描述信息：
 **/
@RestController
public class RocketMQController {
    @Resource(name = "cmdProducer")
    private DefaultMQProducer defaultMQProducer;
    @Resource(name = "transactionMQProducer")
    private TransactionMQProducer transactionMQProducer;


    @RequestMapping("/xiaoxi")
    public String msg(String str) throws Exception{
        Message msg = new Message("MICRO_WX_MSG_TASK_RESULT_TPOIC","TagA","OrderID00",str.getBytes());
        defaultMQProducer.send(msg);
        return "OK";
    }
}
