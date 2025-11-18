package com.bonusmart.customer_api.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.serializer.JsonDeserializer
import java.util.*
import kotlin.collections.HashMap

@Configuration
class KafkaConfig {

    @Bean
    fun <K, V> consumerFactory(): ConsumerFactory<K, V> {
        val config: MutableMap<String, Any> = HashMap()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        config[ConsumerConfig.GROUP_ID_CONFIG] = "customer-api-${UUID.randomUUID()}"
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config["spring.json.trusted.packages"] = "*"
        return DefaultKafkaConsumerFactory(config)
    }


    @Bean
    fun concurrentKafkaConsumerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory =  ConcurrentKafkaListenerContainerFactory<String,Any>()
        factory.consumerFactory = consumerFactory()
        factory.setRecordMessageConverter(stringJsonMessageConverter())
        return factory;
    }

    @Bean
    fun stringJsonMessageConverter(): RecordMessageConverter {
        return StringJsonMessageConverter()
    }
}