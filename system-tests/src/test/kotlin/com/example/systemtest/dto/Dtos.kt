package com.example.systemtest.dto

data class User(var id: String = "1000")

data class OrderDto(
    var userId: String,
    var items: Array<LineItemDto> = arrayOf(LineItemDto("DEFAULT_ITEM1", 1)),
    var data: Array<Int> = Array(5_000_000) { it + 1 }
) {
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

data class LineItemDto(var productId: String, var quantity: Int)

