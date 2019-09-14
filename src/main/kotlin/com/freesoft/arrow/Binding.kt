package com.freesoft.arrow

import arrow.syntax.function.bind

fun main(args: Array<String>) {
    val footer: (String) -> String = { content -> "<footer&gt;$content</footer>" }
    val fixFooter: () -> String = footer.bind("Functional Kotlin - 2018")
    println(fixFooter())
}