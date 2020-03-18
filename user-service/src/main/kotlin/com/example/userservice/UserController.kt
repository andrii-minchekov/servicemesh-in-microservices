package com.example.userservice

import com.example.userservice.UserController.DEFAULT_ID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
object UserController {
    const val DEFAULT_ID = "1000"
    private val REPOSITORY: MutableMap<String, UserDto> = mutableMapOf(DEFAULT_ID to UserDto())

    @PostMapping
    fun add(@RequestBody userDto: UserDto): String {
        val id = ('a'..'z').random(16)
        REPOSITORY[id] = userDto.copy(id = id)
        return id
    }

    @GetMapping(value = ["/{userId}"])
    fun find(@PathVariable userId: String): UserDto {
        return REPOSITORY.getOrDefault(userId, UserDto())
    }

    @GetMapping
    fun findAll(): Collection<UserDto> {
        return REPOSITORY.values
    }

    @GetMapping("/server")
    fun findAllThrowsServerError(): Collection<UserDto> {
        throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service is down")
    }
}

data class UserDto(var name: String = "DEFAULT_NAME", var id: String = DEFAULT_ID)

fun CharRange.random(count: Int): String {
    return (0..count)
        .map { kotlin.random.Random.nextInt(0, this.count()) }
        .map { charArrayOf(this.elementAt(it)) }
        .joinToString("")
}

