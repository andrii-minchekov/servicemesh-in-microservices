package com.example.orderservice.rest.error

import org.springframework.http.HttpStatus

open class RestException(override val message: String, val status: HttpStatus, val code : Int) : Throwable(message)
