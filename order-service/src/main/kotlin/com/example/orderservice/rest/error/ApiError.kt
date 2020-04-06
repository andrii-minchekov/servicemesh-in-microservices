package com.example.orderservice.rest.error

data class ApiError(
    val code: String,
    val message: String,
    val subErrors: List<ApiSubError> = listOf()
) {
    lateinit var traceId: String

    constructor(
        traceId: String,
        code: String,
        message: String,
        subErrors: List<ApiSubError> = listOf()
    ) : this(code, message, subErrors) {
        this.traceId = traceId
    }
}

data class ApiSubError(
    val objectName: String,
    val fieldName: String,
    val rejectedValue: Any?,
    val message: String
)