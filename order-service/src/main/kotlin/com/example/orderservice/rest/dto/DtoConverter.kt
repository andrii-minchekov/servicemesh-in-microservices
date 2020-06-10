package com.example.orderservice.rest.dto

import com.example.orderservice.domain.order.LineItem
import com.example.orderservice.domain.order.Order

fun LineItem.toDto() = LineItemDto(this.productId, this.quantity)

fun LineItemDto.toModel(): LineItem = LineItem(productId = this.productId, quantity = this.quantity)

fun Order.toDto() = OrderDto(this.id, this.userId, this.lineItems.map { it.toDto() }.toTypedArray())