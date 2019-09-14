package com.freesoft.category

class CategoryMain

fun main(args: Array<String>) {
    println(Option.Some("hello world").map { s -> s.toUpperCase() })

    val add3AndMultiplyBy2: (Int) -> Int = { i: Int -> i + 3 }.map { j -> j * 2 }

    println(add3AndMultiplyBy2(0))
    println(add3AndMultiplyBy2(1))
    println(add3AndMultiplyBy2(2))

    println(calculateDiscount(Option.Some(80.0)))
    println(calculateDiscount(Option.Some(30.0)))
    println(calculateDiscount(Option.None))

    val maybeFive = Option.Some(5)
    val maybeTwo = Option.Some(2)

    println(maybeFive.flatMap { f ->
        maybeTwo.map { t ->
            f + t
        }
    })

    val numbers = listOf(1, 2, 3)
    val functions = listOf<(Int) -> Int>({ i -> i * 2 }, { i -> i + 3 })

    val result = numbers.flatMap { number -> functions.map { f -> f(number) } }.joinToString()

    println(result)
}

fun calculateDiscount(price: Option<Double>) = price.flatMap { p ->
    if (p > 50.0) {
        Option.Some(5)
    } else {
        Option.None
    }
}