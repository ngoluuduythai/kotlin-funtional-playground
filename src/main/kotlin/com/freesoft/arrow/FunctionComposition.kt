package com.freesoft.arrow

import arrow.syntax.function.andThen
import arrow.syntax.function.compose
import arrow.syntax.function.forwardCompose
import java.util.*

class FunctionComposition

val p: (String) -> String = { body -> "<p>$body</p>" }

val span: (String) -> String = { body -> "<span>$body/span>" }

val div: (String) -> String = { body -> "<div>$body</div>" }

val strong: (String) -> String = { body -> "<strong>$body</strong>" }

val randomNames: () -> String = {
    if (Random().nextInt() % 2 == 0) {
        "foo"
    } else {
        "bar"
    }
}

fun main(args: Array<String>) {
    val divStrong: (String) -> String = div compose strong

    val spanP: (String) -> String = p forwardCompose span

    val randomStrong: () -> String = randomNames andThen strong

    println(divStrong("Hello compositional world"))
    println(spanP("Hello compositional world"))
    println(randomStrong())
}