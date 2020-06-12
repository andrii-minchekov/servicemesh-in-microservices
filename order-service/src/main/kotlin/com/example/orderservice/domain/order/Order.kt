package com.example.orderservice.domain.order

import com.example.orderservice.random

class Order(val userId: String, val lineItems: List<LineItem>, val data: Array<Int> = arrayOf()) {
    val id: String = ('a'..'z').random(16)

    override fun toString(): String {
        return "Order {$id} has data size {${data.size}}"
    }
}