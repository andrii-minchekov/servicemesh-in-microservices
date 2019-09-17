package com.example.orderservice.domain.order


interface OrderUseCasesApi {

    fun createOrder(order: Order)
    fun findOrderBy(orderId: String): Order?
    fun findAllOrders(): Collection<Order>
}
