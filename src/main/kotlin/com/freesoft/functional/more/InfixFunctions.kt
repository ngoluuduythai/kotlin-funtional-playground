package com.freesoft.functional.more

infix fun Int.add(i: Int) = this + i

object All {
    infix fun your(base: Pair<Base, Us>) {}
}

object Base {
    infix fun are(belong: Belong) = this
}

object Belong

object Us

fun main(args: Array<String>) {

    println(1 add 2)
    println(1.add(2))

    All your (Base are Belong to Us)
}