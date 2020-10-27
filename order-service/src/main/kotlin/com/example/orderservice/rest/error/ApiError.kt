package com.example.orderservice.rest.error

/**
 * Example:
 * `{
 *    "code":"order-4002",
 *    "message":"Input fields contain errors",
 *    "traceId":"7f006775-04b5-4f81-8250-a85ffb976722",
 *    "subErrors":[
 *    {
 *        "objectName":"orderDto",
 *        "fieldName":"userId",
 *        "rejectedValue":"1",
 *        "message":"size must be between 36 and 36"
 *    }
 *    ]
 * }`
 */
data class ApiError(
    /**
     * Internal code to classify error
     *
     * pattern="${serviceNamePrefix}-${httpErrorCode}${sequenceNumberUniqueForServiceNameAndHttpErrorCode}".
     *
     * examples=["orderservice-4001", "orderservice-4002","orderservice-5001", "userservice-4001", "userservice-4002", "userservice-5001"]
     */
    val code: String,
    /**
     * Human readable localized message to display on client side
     */
    val message: String,
    /**
     * Unique identifier of user request.
     * In case of distributed architecture this identifier is passed to all downstream requests to other services.
     */
    val traceId: String,
    /**
     * Collect information about sub errors,
     * for example specific fields of forms providing human readable error messages for each field to guide user trough out a flow
     */
    val subErrors: List<ApiSubError> = listOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ApiError
        if (code != other.code) return false
        if (message != other.message) return false
        if (subErrors != other.subErrors) return false
        return true
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + subErrors.hashCode()
        return result
    }
}

data class ApiSubError(
    val objectName: String,
    val fieldName: String,
    val rejectedValue: Any?,
    val message: String
)