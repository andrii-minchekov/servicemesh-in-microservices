package com.example.orderservice

import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.Mockito

fun <T> String.deserialize(claz: Class<T>): T {
    return ObjectMapper().readValue(this, claz)
}

fun <T> any(): T {
    Mockito.any<T>()
    return null as T
}

