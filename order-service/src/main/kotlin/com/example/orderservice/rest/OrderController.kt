package com.example.orderservice.rest

import com.example.orderservice.domain.order.Order.Companion.USER_ID
import com.example.orderservice.domain.order.OrderUseCasesApi
import com.example.orderservice.rest.dto.OrderView
import com.example.orderservice.rest.dto.User
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(
        "/orders",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class OrderController(val restTemplate: RestTemplate, val orderUseCases: OrderUseCasesApi) {

    val log = LoggerFactory.getLogger(OrderController::class.java)!!
    private var serverFailures: Int = 0
    private var clientFailures: Int = 0

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody orderView: OrderView): String {
        val order = orderView.toDomain()
        orderUseCases.createOrder(order)
        return order.id
    }

    @GetMapping(value = ["/{orderId}"])
    fun find(@PathVariable orderId: String): OrderView {
        return OrderView.from(orderUseCases.findOrderBy(orderId))
    }

    @GetMapping
    fun findAll(): Collection<OrderView> {
        validateUserAccess()
        return orderUseCases.findAllOrders().map { domain -> OrderView.from(domain) }
    }

    @GetMapping(value = ["/server"])
    fun findAllFailureAcccrualServer(): Collection<OrderView> {
        if (serverFailures == 3) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "500 error")
        }
        serverFailures++
        return orderUseCases.findAllOrders().map { domain -> OrderView.from(domain) }
    }


    @GetMapping(value = ["/client"])
    fun findAllFailureAcccrualClient(): Collection<OrderView> {
        if (clientFailures == 3) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "400 error")
        }
        clientFailures++
        return orderUseCases.findAllOrders().map { domain -> OrderView.from(domain) }
    }


    private fun validateUserAccess() {
        restTemplate.getForObject("http://user-service/users/$USER_ID", User::class.java)
                ?: throw IllegalArgumentException("User with id $USER_ID doesn't exist")
    }
}
