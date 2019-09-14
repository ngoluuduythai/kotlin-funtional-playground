package com.freesoft.arrow

import arrow.core.PartialFunction
import arrow.core.invokeOrElse
import arrow.core.orElse
import arrow.syntax.function.pipe

fun main(args: Array<String>) {

    val upper: (String?) -> String = { s: String? -> s!!.toUpperCase() }

    val partialUpper: PartialFunction<String?, String> = PartialFunction(definedAt = { s: String? -> s != null }, ifDefined = upper)

    val upperForNull: PartialFunction<String?, String> = PartialFunction({ s -> s == null }) { "NULL" }

    val totalUpper: PartialFunction<String?, String> = partialUpper orElse upperForNull

    val strings = listOf("one", "two", null, "four")
    strings.map(totalUpper).forEach { s -> println(s) }

    "ceva" pipe partialUpper pipe ::println
}