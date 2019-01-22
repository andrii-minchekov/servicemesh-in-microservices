package com.example.userservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.CommonsRequestLoggingFilter


@SpringBootApplication
class UserServiceApplication {

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
    runApplication<UserServiceApplication>(*args)
}

