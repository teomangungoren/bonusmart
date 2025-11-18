package com.bonusmart.customer_api.listener

import com.bonusmart.customer_api.model.dto.CustomerMessage
import com.bonusmart.customer_api.service.CustomerApplicationService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CustomerCreatedListener(private val customerService: CustomerApplicationService) {


    @KafkaListener(topics = ["customer-created"], groupId = "customer-api",containerFactory = "concurrentKafkaConsumerFactory")
    fun listen(@Payload message: CustomerMessage) {
        customerService.createCustomer(message)
    }

}