package com.example.orderservice

import org.hobsoft.spring.resttemplatelogger.LoggingCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.CommonsRequestLoggingFilter
import javax.servlet.http.HttpServletRequest


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

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}

class CustomRequestLoggingFilter : CommonsRequestLoggingFilter() {

    init {
        super.setBeforeMessagePrefix("REQUEST STARTED : ")
    }

    override fun isIncludeHeaders(): Boolean {
        return true
    }

    override fun isIncludeQueryString(): Boolean {
        return true
    }

    override fun afterRequest(request: HttpServletRequest, message: String) {
        super.afterRequest(request,"REQUEST FINISHED")
    }
}

