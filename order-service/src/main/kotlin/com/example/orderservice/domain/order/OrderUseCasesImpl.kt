package com.example.orderservice.domain.order

import org.springframework.stereotype.Service

@Service
class OrderUseCasesImpl(val repo: OrderRepository) : OrderUseCases {
    override fun findAllOrders(): Collection<Order> {
        return repo.findAll()
    }

    override fun findOrderBy(orderId: String): Order? {
        return repo.findOrderBy(orderId)
    }

    override fun createOrder(order: Order) {
        repo.saveOrder(order)
    }
}

fun CharRange.random(count: Int): String {
    return (0..count)
            .map { kotlin.random.Random.nextInt(0, this.count()) }
            .map { this.elementAt(it) }
            .joinToString("")
}