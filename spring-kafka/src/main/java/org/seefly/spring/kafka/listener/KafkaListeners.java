package org.seefly.spring.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.seefly.spring.kafka.model.DataDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author liujianxin
 * @date 2021/8/20 9:51
 */
@Slf4j
@Component
public class KafkaListeners {

    private static final ObjectMapper mapper = new ObjectMapper();


    @KafkaListener(topics = {"${topic}"}, containerFactory = "ackContainerFactory")
    public void listener(ConsumerRecord<String, byte[]> record, Acknowledgment ack) throws IOException {
        DataDto dataDto = mapper.readValue(record.value(), DataDto.class);
        log.info("{}", dataDto);
        if("3".equals(dataDto.getData())) {
            ack.acknowledge();
        }
    }


}
