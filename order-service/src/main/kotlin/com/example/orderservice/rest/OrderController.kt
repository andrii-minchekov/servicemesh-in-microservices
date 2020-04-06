package com.example.orderservice.rest

import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.OrderUseCasesApi
import com.example.orderservice.integration.UserServiceClient
import com.example.orderservice.rest.dto.OrderDto
import com.example.orderservice.rest.dto.toDto
import com.example.orderservice.rest.error.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(
    "/orders",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class OrderController(val userServiceClient: UserServiceClient, val orderUseCases: OrderUseCasesApi) {

    val log: Logger = LoggerFactory.getLogger(OrderController::class.java)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@Valid @RequestBody dto: OrderDto): String {
        return orderUseCases.create(Order(dto.userId, dto.items.map { it.toModel() })).id
    }

    @GetMapping(value = ["/{orderId}"])
    fun find(@PathVariable orderId: String): OrderDto {
        return orderUseCases.findOne(orderId)?.let { it.toDto() }
            ?: throw EntityNotFoundException("Entity with id $orderId not found")
    }

    @GetMapping
    fun findAll(): Collection<OrderDto> {
        userServiceClient.validateUserAccess()
        return orderUseCases.findAll().map { it.toDto() }
    }
}