package com.example.orderservice.rest.error

import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import java.util.*

object ApiErrorFactory {
    fun error(e: RestException): ApiError {
        //TODO fix uuid to distributed tracing id from context
        return ApiError(serviceCode(e.code), e.message, uniqueId())
    }

    fun error(e: MethodArgumentNotValidException) = ApiError(
        serviceCode(4002),
        "Input fields contain errors",
        uniqueId(),
        listOf(*e.bindingResult.fieldErrors.map(subError).toTypedArray())
    )

    private val subError = { fe: FieldError ->
        ApiSubError(fe.objectName, fe.field, fe.rejectedValue, fe.defaultMessage ?: "")
    }

    private fun uniqueId() = UUID.randomUUID().toString()

    private fun serviceCode(code: Int) = "order-${code}"
}