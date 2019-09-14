package com.freesoft.category

object Function1 {
    fun <A, B> pure(b: B) = { _: A -> b }

    
}

fun main(args: Array<String>) {
    val f: (String) -> Int = Function1.pure(0)
    println(f("Hello"))
}