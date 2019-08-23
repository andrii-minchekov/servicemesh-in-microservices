package com.example.orderservice

import com.example.orderservice.dto.Order
import com.example.orderservice.dto.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
class OrderController(@Autowired val restTemplate: RestTemplate) {

    val log = LoggerFactory.getLogger(OrderController::class.java)

    companion object {
        const val DEFAULT_ORDER_ID = "10000"
        const val USER_ID = "1000"
    }

    private val repository: MutableMap<String, Order> = mutableMapOf(DEFAULT_ORDER_ID to Order())

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody order: Order): String {
        val id = ('a'..'z').random(16)
        repository[id] = order.copy(id = id)
        return id
    }

    @PostMapping(value = ["/ids"], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun registerIds(@RequestBody ids: Array<Int>): Boolean {
        ids.forEach {
            repository[it.toString()] = Order(id = it.toString())
        }
        log.info("Registered ${ids.size} ids")
        return true
    }

    @GetMapping(value = ["/{orderId}"])
    fun find(@PathVariable orderId: String): Order {
        return repository.getOrDefault(orderId, Order())
    }

    @GetMapping
    fun findAll(): Collection<Order> {
        validateUserAccess()
        return repository.values.filter { it.userId == USER_ID }
    }

    private var serverFailures: Int = 0
    private var clientFailures: Int = 0

    @GetMapping(value = ["/server"])
    fun findAllFailureAcccrualServer(): Collection<Order> {
        if (serverFailures == 3) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "500 error")
        }
        serverFailures++
        return repository.values.filter { it.userId == USER_ID }
    }


    @GetMapping(value = ["/client"])
    fun findAllFailureAcccrualClient(): Collection<Order> {
        if (clientFailures == 3) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "400 error")
        }
        clientFailures++
        return repository.values.filter { it.userId == USER_ID }
    }


    private fun validateUserAccess() {
        restTemplate.getForObject("http://user-service/users/$USER_ID", User::class.java)
            ?: throw IllegalArgumentException("User with id $USER_ID doesn't exist")
    }
}

fun CharRange.random(count: Int): String {
    return (0..count)
        .map { kotlin.random.Random.nextInt(0, this.count()) }
        .map { this.elementAt(it) }
        .joinToString("")
}

