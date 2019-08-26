package com.example.orderservice.domain.order

interface OrderRepository {
    fun saveOrder(order: Order)
    fun findOrder(orderId: String): Order?
}