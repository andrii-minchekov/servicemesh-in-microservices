package com.example.orderservice.domain.order

import com.example.orderservice.domain.order.Order.Companion.DEFAULT_ORDER_ID
import com.example.orderservice.domain.order.Order.Companion.DEFAULT_PRODUCT_ID
import com.example.orderservice.domain.order.Order.Companion.USER_ID
import org.springframework.stereotype.Repository

/**
 * Component specifying all set of operations with with order persistent storage
 */
interface OrderRepository {
    fun saveOrder(order: Order)
    fun findOrderBy(orderId: String): Order?
    fun findAll(): Collection<Order>

    @Repository
    class InMemOrderRepository : OrderRepository {

        override fun findAll(): Collection<Order> {
            return repository.values.toList()
        }

        override fun findOrderBy(orderId: String): Order? {
            return repository[orderId]
        }

        override fun saveOrder(order: Order) {
            repository[order.id] = order
        }

        private val repository: MutableMap<String, Order> = mutableMapOf(DEFAULT_ORDER_ID to Order(USER_ID, listOf(OrderItem(DEFAULT_PRODUCT_ID, 100))))
    }
}