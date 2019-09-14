package com.freesoft.arrow

import arrow.syntax.function.memoize
import kotlin.system.measureNanoTime


fun recursiveFib(n: Long): Long = if (n < 2) {
    n
} else {
    recursiveFib(n - 1) + recursiveFib(n - 2)
}

fun imperativeFib(n: Long): Long {
    return when (n) {
        0L -> 0
        1L -> 1
        else -> {
            var a = 0L
            var b = 1L
            var c = 0L
            for (i in 2..n) {
                c = a + b
                a = b
                b = c
            }
            c
        }
    }
}

inline fun milliseconds(description: String, body: () -> Unit): String {
    return "$description:${measureNanoTime(body) / 1_000_000.00} ms"
}

fun main(args: Array<String>) {
    var lambdaFib: (Long) -> Long = { it }

    lambdaFib = { n: Long -> if (n < 2) n else lambdaFib(n - 1) + lambdaFib(n - 2) }

    var memoizedFib: (Long) -> Long = { it }

    memoizedFib = { n: Long -> if (n < 2) n else memoizedFib(n - 1) + memoizedFib(n - 2) }.memoize()

    println(milliseconds("imperative fib") { imperativeFib(40) }) //0.006

    println(milliseconds("recursive fib") { recursiveFib(40) }) //1143.167
    println(milliseconds("lambda fib") { lambdaFib(40) }) //4324.890
    println(milliseconds("memoized fib") { memoizedFib(40) }) //1.588
}