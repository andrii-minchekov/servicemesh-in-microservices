package com.example.orderservice.domain.order

class Order(val userId : String = USER_ID, val lineItems: List<OrderItem>) {
    val id: String = ('a'..'z').random(16)

    companion object {
        const val DEFAULT_ORDER_ID = "10000"
        const val USER_ID = "1000"
        const val DEFAULT_PRODUCT_ID = "100"
    }
}