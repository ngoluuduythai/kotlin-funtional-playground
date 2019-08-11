package com.freesoft.functional.dsl


fun main(args: Array<String>) {
    val joinWithPipe = with(listOf("One", "Two", "Three")) {
        joinToString(separator = "|")
    }
    println(joinWithPipe)
}