package com.example.orderservice

fun CharRange.random(count: Int): String {
    return (0..count)
        .map { kotlin.random.Random.nextInt(0, this.count()) }
        .map { this.elementAt(it) }
        .joinToString("")
}