package com.example.orderservice

import com.example.orderservice.domain.order.*
import com.example.orderservice.domain.order.OrderRepository.*
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class OrderUseCasesTest {

    @Test
    fun `should create new order in system`() {
        val repo = InMemOrderRepository()
        val useCases = OrderUseCases(repo)
        val order = Order(
            userId = UUID.randomUUID().toString(),
            lineItems = arrayListOf(OrderItem(productId = UUID.randomUUID().toString(), quantity = 10))
        )

        useCases.createOrder(order)

        assertThat(repo.findOrderBy(order.id)).isNotNull
    }
}