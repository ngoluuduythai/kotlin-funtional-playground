package com.freesoft.category

class ApplicativeMain

fun main(args: Array<String>) {
    val numbers = listOf(1, 2, 3)
    val functions = listOf<(Int) -> Int>({ i -> i * 2 }, { i -> i + 3 })

    val result = numbers.flatMap { number -> functions.map { f -> f(number) } }.joinToString()

    println(result)

    val applicativeResult = numbers.ap(functions).joinToString()
    println(applicativeResult)

    val maybeFive = Option.pure(5)
    val maybeTwo = Option.pure(2)

    val fab = maybeTwo.map { f -> { t: Int -> f + t } }
    maybeFive.ap(fab)
//
//    Option.pure { f: Int -> { t: Int -> f + t } } `(*)` maybeFive `(*)` maybeTwo
//
//    Option.pure { f: Int -> { t: Int -> f + t } } `(*)` maybeFive `(*)` maybeTwo
}