package org.seefly.spring.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.CommonClientConfigs;
import org.junit.jupiter.api.Test;
import org.seefly.spring.kafka.model.DataDto;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.JsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class SpringKafkaApplicationTests {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void contextLoads() throws JsonProcessingException {
        Map<String, Object> props = new HashMap<>();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "10.5.4.147:9092,10.5.4.148:9092,10.5.4.149:9092");
        props.put(CommonClientConfigs.CLIENT_ID_CONFIG, "TEMP-KAFKA-PRODUCE");
        props.put("auto.commit.interval.ms", 1000);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);

        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(factory);
        JsonMessageConverter converter = new JsonMessageConverter(mapper);
        kafkaTemplate.setMessageConverter(converter);
        kafkaTemplate.setDefaultTopic("TEMP-KAFKA-TEST");


        DataDto data = new DataDto();
        data.setData("3");
        data.setTimestamp(System.currentTimeMillis());
        kafkaTemplate.sendDefault(mapper.writeValueAsString(data));
    }

}
