package com.example.orderservice.rest

import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.OrderUseCasesApi
import com.example.orderservice.rest.dto.EntityNotFoundException
import com.example.orderservice.rest.dto.OrderDto
import com.example.orderservice.rest.dto.User
import com.example.orderservice.rest.dto.toDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping(
    "/orders",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class OrderController(val restTemplate: RestTemplate, val orderUseCases: OrderUseCasesApi) {

    val log: Logger = LoggerFactory.getLogger(OrderController::class.java)

    companion object {
        const val USER_ID = "1000"
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody dto: OrderDto): String {
        return orderUseCases.create(Order(dto.userId, dto.items.map { it.toModel() })).id
    }

    @GetMapping(value = ["/{orderId}"])
    fun find(@PathVariable orderId: String): OrderDto {
        return orderUseCases.findOne(orderId)?.let {
            OrderDto(it.userId, it.lineItems.map { it.toDto() }.toTypedArray(), it.id)
        } ?: throw EntityNotFoundException("Entity with id $orderId not found")
    }

    @GetMapping
    fun findAll(): Collection<OrderDto> {
        validateUserAccess()
        return orderUseCases.findAll().map { it.toDto() }
    }

    private fun validateUserAccess() {
        restTemplate.getForObject("http://user-service/users/$USER_ID", User::class.java)
            ?: throw IllegalArgumentException("User with id $USER_ID doesn't exist")
    }
}