package com.example.orderservice.rest.dto

import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.Order.Companion.DEFAULT_ORDER_ID
import com.example.orderservice.domain.order.OrderItem
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

abstract class Dto {
    fun toJson(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}

data class User(var id: String = "1000") : Dto()

data class OrderView(
        var userId: String = "1000",
        var items: List<OrderItemView> = listOf(OrderItemView("DEFAULT_PRODUCT_ID", 1)),
        var id: String = DEFAULT_ORDER_ID
) : Dto() {

    companion object {
        fun from(domain: Order?): OrderView {
            if (domain != null) {
                return OrderView(domain.userId, OrderItemView.from(domain.lineItems), domain.id)
            }
            throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            )
        }
    }

    fun toDomain(): Order {
        return Order(this.userId, this.items.map { dto -> dto.toDomain() })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderView

        if (!items.containsAll(other.items)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + items.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}

data class OrderItemView(val productId: String, val quantity: Int) : Dto() {

    fun toDomain(): OrderItem {
        return OrderItem(this.productId, this.quantity)
    }

    companion object {
        fun from(domain: OrderItem) = OrderItemView(domain.productId, domain.quantity)
        fun from(elements: List<OrderItem>) = elements.map { e -> OrderItemView(e.productId, e.quantity) }
    }
}