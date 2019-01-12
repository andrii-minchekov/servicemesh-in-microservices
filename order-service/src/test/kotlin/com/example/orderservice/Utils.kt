package com.example.orderservice

import com.fasterxml.jackson.databind.ObjectMapper

fun <T> String.deserializeFromJson(claz: Class<T>): T {
    return ObjectMapper().readValue(this, claz)
}

