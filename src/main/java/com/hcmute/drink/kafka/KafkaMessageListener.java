package com.hcmute.drink.kafka;

import com.hcmute.drink.dto.CodeEmailDto;
import com.hcmute.drink.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageListener {
    private final EmailUtils emailUtils;


    @KafkaListener(topics = KafkaConstant.SEND_CODE_EMAIL_TOPIC)
    public void consumeSendCodeEmail(CodeEmailDto message) {
        log.info("Kafka Consumer {}", message.toString());
        if(message != null) {
            emailUtils.sendHtmlVerifyCodeToRegister(message.getEmail(), message.getCode());
        }
    }
}
