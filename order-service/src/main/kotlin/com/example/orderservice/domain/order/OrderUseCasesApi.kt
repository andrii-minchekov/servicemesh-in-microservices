package com.example.orderservice.domain.order


interface OrderUseCasesApi {

    fun create(order: Order): Order
    fun findOne(orderId: String): Order?
    fun findAll(): List<Order>
}
