package com.freesoft.functional.collections

fun main(args: Array<String>) {
    val list = listOf(10, 20, 30)
    val flatMappedList = list.flatMap { it.rangeTo(it + 2) }
    println("flatMappedList = $flatMappedList")
}