package com.example.orderservice

import com.example.orderservice.rest.filter.CustomRequestLoggingFilter
import org.hobsoft.spring.resttemplatelogger.LoggingCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}

@SpringBootApplication
class OrderServiceApplication {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .customizers(LoggingCustomizer())
            .build()
    }

    @Bean
    fun logFilter(): CustomRequestLoggingFilter {
        return CustomRequestLoggingFilter()
    }
}


