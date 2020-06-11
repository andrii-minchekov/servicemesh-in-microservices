package com.example.orderservice.domain.order

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["distributed-cache.enabled"], havingValue = "false", matchIfMissing = true)
class InMemOrderRepository : OrderRepository {

    private val orders: MutableMap<String, Order> = mutableMapOf()

    override fun findOne(orderId: String): Order? {
        return orders[orderId]
    }

    override fun findAll(): Collection<Order> {
        return orders.values.toList()
    }

    override fun save(order: Order): Order {
        orders[order.id] = order
        return order
    }
}