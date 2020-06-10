package com.example.orderservice.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import javax.validation.constraints.Size

abstract class Dto {
    fun toJson(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}

data class User(var id: String = "1000") : Dto()

data class OrderDto(
    @JsonProperty("userId")
    @get:Size(min = 1, max = 36)
    var userId: String,
    @JsonProperty("items")
    var items: Array<LineItemDto> = arrayOf(LineItemDto("DEFAULT_ITEM1", 1))
) : Dto() {
    @JsonProperty("id")
    var id: String? = null

    constructor(id: String, userId: String, items: Array<LineItemDto>) : this(userId, items) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderDto

        if (!items.contentEquals(other.items)) return false
        if (id != other.id) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = items.contentHashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}

data class LineItemDto(val productId: String, val quantity: Int) : Dto()

