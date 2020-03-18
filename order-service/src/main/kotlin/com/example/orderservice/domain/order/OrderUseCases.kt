package com.example.orderservice.domain.order

import org.springframework.stereotype.Service

@Service
class OrderUseCases(val repo: OrderRepository) : OrderUseCasesApi {

    override fun create(order: Order): Order {
        return repo.save(order)
    }

    override fun findAll(): List<Order> {
        return repo.findAll()
    }

    override fun findOne(orderId: String): Order? {
        return repo.findOne(orderId)
    }
}