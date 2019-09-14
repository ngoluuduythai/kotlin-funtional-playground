package com.freesoft.arrow

import arrow.syntax.function.pipe

fun main(args: Array<String>) {

    val strong: (String) -> String = { body -> "<strong>$body</strong>" }

    "From a pipe".pipe(strong).pipe(::println)

    Quote(20.0, "Foo", "Shoes", 1) pipe ::calculatePrice pipe ::filterBills pipe ::splitter

}