package com.example.userservice

import com.example.userservice.UserController.DEFAULT_ID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
object UserController {
    const val DEFAULT_ID = "1000"
    private val repository: MutableMap<String, User> = mutableMapOf(DEFAULT_ID to User())

    @PostMapping
    fun add(@RequestBody user: User): String {
        val id = ('a'..'z').random(16)
        repository[id] = user.copy(id = id)
        return id
    }

    @GetMapping(value = ["/{userId}"])
    fun find(@PathVariable userId: String): User {
        return repository.getOrDefault(userId, User())
    }

    @GetMapping
    fun findAll(): Collection<User> {
        return repository.values
    }

    @GetMapping("/server")
    fun findAllThrowsServerError(): Collection<User> {
        throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service is down")
    }
}

data class User(var name: String = "DEFAULT_NAME", var id: String = DEFAULT_ID)

fun CharRange.random(count: Int): String {
    return (0..count)
        .map { kotlin.random.Random.nextInt(0, this.count()) }
        .map { charArrayOf(this.elementAt(it)) }
        .joinToString("")
}

