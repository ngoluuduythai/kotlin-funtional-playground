package com.freesoft.functional.collections

val num = listOf(1, 2, 3, 4)

fun main(args: Array<String>) {
    val numbersTwice = num.map { i -> i * 2 }
    numbersTwice.forEach { i -> println(i) }

    val sum = num.sum()
    println(sum)
}
