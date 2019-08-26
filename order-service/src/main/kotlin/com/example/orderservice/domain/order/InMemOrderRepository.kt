package com.example.orderservice.domain.order

import org.springframework.stereotype.Repository

@Repository
class InMemOrderRepository : OrderRepository {

    private val orders : MutableMap<String, Order> = mutableMapOf()

    override fun findOrder(orderId: String): Order? {
        return orders[orderId]
    }

    override fun saveOrder(order: Order) {
        orders[order.id] = order
    }
}