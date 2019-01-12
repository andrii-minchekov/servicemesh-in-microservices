package com.example.userservice

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
object UserController {
    private val cache: MutableMap<String, User> = HashMap()

    @RequestMapping(method = [RequestMethod.POST])
    fun addUser(@RequestBody user: User): String {
        val id = ('a'..'z').random(16)
        cache[id] = user.copy(id = id)
        return id
    }

    @RequestMapping(value = ["/{userId}"])
    fun findUser(@PathVariable userId: String): User {
        return cache.getOrDefault(userId, User())
    }
}

data class User(var name: String = "DEFAULT_NAME", var id: String = "DEFAULT_ID") {
}

fun CharRange.random(count: Int): String {
    return (0..count)
        .map { kotlin.random.Random.nextInt(0, this.count()) }
        .map { charArrayOf(this.elementAt(it)) }
        .joinToString("")
}

