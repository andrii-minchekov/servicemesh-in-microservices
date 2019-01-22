package com.example.orderservice

import org.hobsoft.spring.resttemplatelogger.LoggingCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.CommonsRequestLoggingFilter


@SpringBootApplication
class OrderServiceApplication {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .customizers(LoggingCustomizer())
            .build()
    }

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(false)
        filter.setMaxPayloadLength(10000)
        filter.setIncludeHeaders(true)
        filter.setAfterMessagePrefix("REQUEST DATA : ")
        return filter
    }
}

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}

