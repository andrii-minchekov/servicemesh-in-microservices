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

    companion object {
        const val BEFORE = "REQUEST STARTED : "
        const val AFTER = "REQUEST FINISHED."
        const val PROMETHEUS = "uri=/actuator/prometheus"
    }

    init {
        super.setBeforeMessagePrefix(BEFORE)
        super.setAfterMessagePrefix(AFTER)
    }

    override fun isIncludeHeaders(): Boolean {
        return true
    }

    override fun isIncludeQueryString(): Boolean {
        return true
    }

    override fun beforeRequest(request: HttpServletRequest, message: String) {
        if (!message.startsWith("$BEFORE$PROMETHEUS")) {
            super.beforeRequest(request, message)
        }
    }

    override fun afterRequest(request: HttpServletRequest, message: String) {
        if (!message.startsWith("$AFTER$PROMETHEUS")) {
            super.afterRequest(request, AFTER)
        }
    }
}

