package com.twentyone.steachserver.config;

import com.twentyone.steachserver.interfaces.kafka.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {
    private Properties props;

    @Bean
    public Properties kafkaProperties() {
        props = new Properties();
        // Broker 정보를 정의한다.
        props.put("bootstrap.servers", "peter-kafka01.foo.bar:9092,peter-kafka02.foo.bar:9092,peter-kafka02.foo.bar:9092");
        // Record의 key와 value는 문자이기 때문에 전송시 byte로 변환해야한다.
        // 각각 serializer로 kafka에서 제공하는 StringSerializer를 사용하도록 한다.
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }

//    @Bean
//    public KafkaProducer<String, String> kafkaProducer() {
//        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
//        try {
//            for (int i = 0; i < 3; i++) {
//                // Producer가 보낼 Record를 생성한다.
//                ProducerRecord<String, String> record = new ProducerRecord<>("peter-basic01",
//                        "Hello This is Kafka Record - " + i);
//                // Producer가 Record를 보낸다.
//                producer.send(record);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            producer.close();
//        }
//
//    }
}
