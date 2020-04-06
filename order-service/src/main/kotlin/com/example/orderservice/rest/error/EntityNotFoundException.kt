package com.example.orderservice.rest.error

import org.springframework.http.HttpStatus

data class EntityNotFoundException(override val message: String) : RestException(message, HttpStatus.NOT_FOUND, 4001)
