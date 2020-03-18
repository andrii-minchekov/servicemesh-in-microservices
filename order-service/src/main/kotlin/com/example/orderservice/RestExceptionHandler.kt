package com.example.orderservice

import com.example.orderservice.rest.dto.ApiError
import com.example.orderservice.rest.dto.ClientException
import com.example.orderservice.rest.dto.ServerException
import org.slf4j.LoggerFactory
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
    fun handleCustom(ex: Throwable, request: WebRequest): ResponseEntity<ApiError> {
        LoggerFactory.getLogger(RestExceptionHandler::class.java).info("About to handle an exception: ", ex)
        val msg = "Specify user visible error"
        if (ex is ServerException || ex is IllegalStateException) {
            return buildResponseEntity(ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, ex))
        }
        return buildResponseEntity(ApiError(HttpStatus.BAD_REQUEST, msg, ex))
    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<ApiError> {
        return ResponseEntity(apiError, apiError.status)
    }
}