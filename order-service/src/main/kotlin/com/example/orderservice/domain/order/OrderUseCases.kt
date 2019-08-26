package com.example.orderservice.domain.order

import org.springframework.stereotype.Service

@Service
class OrderUseCases(val repo: OrderRepository) : OrderUseCasesApi {

    override fun createOrder(order: Order) {
        repo.saveOrder(order)
    }
}