package com.freesoft.functional.more

fun <T, R> transform(vararg ts: T, f: (T) -> R): List<R> = ts.map(f)

fun main(args: Array<String>) {
    val transform = transform(1, 2, 3, 4) { i -> i.toString() }
    println(transform)


}