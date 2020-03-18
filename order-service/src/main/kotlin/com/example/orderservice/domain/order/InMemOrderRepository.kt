package com.example.orderservice.domain.order

import org.springframework.stereotype.Repository

@Repository
class InMemOrderRepository : OrderRepository {

    private val orders: MutableMap<String, Order> = mutableMapOf()

    override fun findOne(orderId: String): Order? {
        return orders[orderId]
    }

    override fun findAll(): List<Order> {
        return orders.values.toList()
    }

    override fun save(order: Order): Order {
        orders[order.id] = order
        return order
    }
}