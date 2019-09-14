package com.freesoft.arrow

import arrow.syntax.function.curried
import arrow.syntax.function.pipe
import arrow.syntax.function.reverse
import arrow.syntax.function.uncurried

fun main(args: Array<String>) {

    val strong: (String, String, String) -> String = { body, id, style -> "<strong id=\"$id\" style=\"$style\">$body</strong>" }

    val curriedStrong: (style: String) -> (id: String) -> (body: String) -> String = strong.reverse().curried()

    val greenStrong: (id: String) -> (body: String) -> String = curriedStrong("color:green")

    val uncurriedGreenStrong: (id: String, body: String) -> String = greenStrong.uncurried()

    println(greenStrong("movie5")("Green Inferno"))
    println(uncurriedGreenStrong("movie6", "Green hornet"))
    "Fried green tomatoes" pipe ("movie 7" pipe greenStrong) pipe ::println

}