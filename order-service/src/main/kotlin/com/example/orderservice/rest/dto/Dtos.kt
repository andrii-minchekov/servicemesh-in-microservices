package com.example.orderservice.rest.dto

import com.example.orderservice.domain.order.LineItem
import com.example.orderservice.domain.order.Order
import com.fasterxml.jackson.databind.ObjectMapper

abstract class Dto {
    fun toJson(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}

data class User(var id: String = "1000") : Dto()

data class OrderDto(
    var userId: String = "1000",
    var items: Array<LineItemDto> = arrayOf(LineItemDto("DEFAULT_ITEM1", 1)),
    var id: String = "10000"
) : Dto() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderDto

        if (!items.contentEquals(other.items)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = items.contentHashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}

fun Order.toDto() = OrderDto(this.userId, this.lineItems.map { it.toDto() }.toTypedArray(), this.id)

data class LineItemDto(val productId: String, val quantity: Int) : Dto() {
    fun toModel(): LineItem = LineItem(productId = this.productId, quantity = this.quantity)
}

fun LineItem.toDto() = LineItemDto(this.productId, this.quantity)