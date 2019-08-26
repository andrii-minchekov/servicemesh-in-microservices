package com.example.orderservice.rest.dto

import com.fasterxml.jackson.databind.ObjectMapper

abstract class Dto {
    fun toJson(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}

data class User(var id: String = "1000"): Dto()

data class Order(
    var userId: String = "1000",
    var items: Array<String> = arrayOf("DEFAULT_ITEM1"),
    var id: String = "10000"
) : Dto() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

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