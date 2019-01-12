package com.example.orderservice

import com.example.orderservice.dto.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    "/orders",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class OrderController {
    private val cache: MutableMap<String, Order> = HashMap()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addOrder(@RequestBody order: Order): String {
        val id = ('a'..'z').random(16)
        cache[id] = order.copy(id = id)
        return id
    }

    @GetMapping(value = ["/{orderId}"])
    fun findOrder(@PathVariable orderId: String): Order {
        return cache.getOrDefault(orderId, Order())
    }

    @GetMapping
    fun hello(): String {
        return "controller is healthy"
    }
}

fun CharRange.random(count: Int): String {
    return (0..count)
        .map { kotlin.random.Random.nextInt(0, this.count()) }
        .map { this.elementAt(it)}
        .joinToString("")
}

