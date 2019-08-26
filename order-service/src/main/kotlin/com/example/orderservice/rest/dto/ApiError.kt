package com.example.orderservice.rest.dto

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime


class ApiError private constructor() {

    lateinit var status: HttpStatus
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now()
    var message: String? = null
    var debugMessage: String? = null
    val subErrors: List<ApiSubError>? = null

    constructor(status: HttpStatus) : this() {
        this.status = status
    }

    constructor(status: HttpStatus, ex: Throwable) : this() {
        this.status = status
        this.message = "Unexpected error"
        this.debugMessage = ex.localizedMessage
    }

    constructor(status: HttpStatus, message: String, ex: Throwable) : this() {
        this.status = status
        this.message = message
        this.debugMessage = ex.localizedMessage
    }
}

abstract class ApiSubError

class ApiValidationError : ApiSubError {
    private val `object`: String
    var field: String? = null
        set(field) {
            this.field = this.field
        }
    var rejectedValue: Any? = null
        set(rejectedValue) {
            this.rejectedValue = this.rejectedValue
        }
    private val message: String

    constructor(`object`: String, message: String) {
        this.`object` = `object`
        this.message = message
    }
}