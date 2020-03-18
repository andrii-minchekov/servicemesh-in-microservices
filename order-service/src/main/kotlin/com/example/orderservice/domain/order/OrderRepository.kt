package com.example.orderservice.domain.order

interface OrderRepository {
    fun save(order: Order): Order
    fun findOne(orderId: String): Order?
    fun findAll(): List<Order>
}