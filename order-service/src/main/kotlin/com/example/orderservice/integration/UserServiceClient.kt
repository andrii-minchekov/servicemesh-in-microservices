package com.example.orderservice.integration

import com.example.orderservice.rest.dto.User
import com.example.orderservice.rest.error.EntityNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class UserServiceClient(val restTemplate: RestTemplate) {
    companion object {
        const val USER_ID = "1000"
    }

    fun validateUserAccess() {
        restTemplate.getForObject("http://user-service/users/$USER_ID", User::class.java)
            ?: throw EntityNotFoundException("User with id $USER_ID doesn't exist")
    }
}

