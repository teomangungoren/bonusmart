package com.bonusmart.product_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
class ProductApiApplication

fun main(args: Array<String>) {
	runApplication<ProductApiApplication>(*args)
}
