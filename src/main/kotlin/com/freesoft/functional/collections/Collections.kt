package com.freesoft.functional.collections

val numbers: List<Int> = listOf(1, 2, 3, 4)

fun main(args: Array<String>) {

    println("imperative style")
    for (i in numbers) {
        println("i= $i")
    }

    println("functional style")
    numbers.forEach { i -> println("i=$i") }

    val twiced = mutableListOf<Int>()
    for (i in numbers) {
        twiced.add(i * 2)
    }

    println("twiced")
    twiced.forEach { i -> println("i=$i") }
}