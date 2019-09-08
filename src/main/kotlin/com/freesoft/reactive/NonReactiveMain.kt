package com.freesoft.reactive

fun main(args: Array<String>) {
    val list = listOf(1, "Two", 3, "Four", "Five", 5.5f)
    val iterator = list.iterator()
    while (iterator.hasNext()) {
        println(iterator.next())
    }
}