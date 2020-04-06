package com.example.orderservice

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.mockito.Mockito

fun <T> String.deserialize(claz: Class<T>): T {
    return jacksonObjectMapper().readValue(this, claz)
}

fun <T> any(): T {
    Mockito.any<T>()
    return null as T
}

