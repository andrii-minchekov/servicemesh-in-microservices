package com.example.orderservice

import com.example.orderservice.rest.error.ApiError
import com.example.orderservice.rest.error.ApiErrorFactory
import com.example.orderservice.rest.error.RestException
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RestException::class)
    fun handleCustom(ex: RestException, request: WebRequest): ResponseEntity<ApiError> {
        LoggerFactory.getLogger(RestExceptionHandler::class.java).info("About to handle an exception: ", ex)
        return ResponseEntity(
            ApiErrorFactory.error(ex),
            ex.status
        )
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders, status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity(ApiErrorFactory.error(ex), status);
    }
}