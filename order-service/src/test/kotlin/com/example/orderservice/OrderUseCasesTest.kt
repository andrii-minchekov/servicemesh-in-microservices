package com.example.orderservice

import com.example.orderservice.domain.order.InMemOrderRepository
import com.example.orderservice.domain.order.LineItem
import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.OrderUseCases
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
            lineItems = arrayListOf(LineItem(productId = UUID.randomUUID().toString(), quantity = 10))
        )
        useCases.create(order)

        assertThat(repo.findOne(order.id)).isNotNull
    }
}