package com.example.systemtest

import io.restassured.response.Response

class RoundRobin<T>(private val elements: Array<T>) : () -> T {
    private var index = 0
    override operator fun invoke(): T {
        val result = elements[index]
        //println ("Selected element is {${elements[index]}}")
        if (index < elements.size - 1) {
            index++
        } else {
            index = 0
        }
        return result
    }
}

fun logResponse(response: Response) {
    println()
    val builder = StringBuilder("RESPONSE:${System.lineSeparator()}")
    builder.append("BODY:", System.lineSeparator(), response.body.asString(), System.lineSeparator())
    builder.append("STATUS:", java.lang.String.valueOf(response.statusCode), System.lineSeparator())
    builder.append("HEADERS:", System.lineSeparator(), response.headers.toString(), System.lineSeparator())
    println(builder.toString())
}