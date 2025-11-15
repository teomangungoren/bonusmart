package com.bonusmart.auth_api.service;

import com.bonusmart.auth_api.model.dto.CustomerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaNotifyService {

    public final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.customer-created}")
    private String customerCreatedTopic;

    @Transactional
    public void notifyCustomer(CustomerMessage customerMessage) {
        kafkaTemplate.send(customerCreatedTopic,customerMessage);
    }
}
