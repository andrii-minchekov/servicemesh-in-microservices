package com.example.orderservice.domain.order

/**
 * Entry point for all business cases related with order management
 */
interface OrderUseCases {

    fun createOrder(order: Order)
    fun findOrderBy(orderId: String): Order?
    fun findAllOrders(): Collection<Order>
}
