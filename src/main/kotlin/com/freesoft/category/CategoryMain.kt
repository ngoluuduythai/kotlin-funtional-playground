package com.freesoft.category

class CategoryMain

fun main(args: Array<String>) {
    println(Option.Some("hello world").map { s -> s.toUpperCase() })

    val add3AndMultiplyBy2 : (Int) -> Int = {i}

}