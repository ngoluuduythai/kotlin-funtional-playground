package com.freesoft.functional.collections

import com.freesoft.functional.collections.FuncList.Cons
import com.freesoft.functional.collections.FuncList.Nil

fun intListOf(vararg numbers: Int): FuncList<Int> {
    return if (numbers.isEmpty()) {
        Nil
    } else {
        Cons(numbers.first(),
                intListOf(*numbers.drop(1).toTypedArray().toIntArray()))
    }
}

fun main(args: Array<String>) {
    val numbers = Cons(1, Cons(2, Cons(3, Cons(4, Nil))))

    val newNumbers = intListOf(1, 2, 3, 4, 5)

    newNumbers.forEach { i -> println(i) }

    val sum = newNumbers.fold(0) { acc, i -> acc + i }

    println("sum = $sum")

    val reversedNumbers = newNumbers.reverse()

    val mappedNumbers = newNumbers.map { i -> i + 1 }
    mappedNumbers.forEach { i -> println("mapped = $i") }

}