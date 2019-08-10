package com.freesoft.functional.collections

val list = listOf(1, 2, 3, 4, 5)

fun main(args: Array<String>) {
    val sum = list.fold(2) { acc, i -> acc + i }
    println(sum)

    val reducedSum = list.reduce { acc, i -> acc + i }
    println(reducedSum)
}