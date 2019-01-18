package com.example.orderservice

import com.example.orderservice.dto.ApiError
import com.example.orderservice.dto.ClientException
import com.example.orderservice.dto.ServerException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ClientException::class, ServerException::class)
    fun handleCustom(ex: Throwable, request: WebRequest): ResponseEntity<Any> {
        val error = "User visible error"
        if (ex is ServerException || ex is IllegalStateException) {
            return buildResponseEntity(ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex))
        }
        return buildResponseEntity(ApiError(HttpStatus.BAD_REQUEST, error, ex))
    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
        return ResponseEntity(apiError, apiError.status)


        //other exception handlers below

    }
}