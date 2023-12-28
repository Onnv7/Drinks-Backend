package com.hcmute.drink.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic createTopic() {
        System.out.println("KAFKA SEND_CODE_EMAIL_TOPIC");
        return new NewTopic(KafkaConstant.SEND_CODE_EMAIL_TOPIC,  5, (short) 1);
    }
}
