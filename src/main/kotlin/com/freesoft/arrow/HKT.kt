package com.freesoft.arrow

import arrow.core.*

fun eitherDivide(num: Int, den: Int): Either<String, Int> {

    val option = optionDivide(num, den)
    return when (option) {
        is Some -> Either.Right(option.t)
        None -> Either.Left("$num isn't divisible by $den")
    }

}

fun eitherDivision(a: Int, b: Int, den: Int): Either<String, Tuple2<Int, Int>> {
    val aDiv = eitherDivide(a, den)
    return when (aDiv) {
        is Either.Right -> {
            val bDiv = eitherDivide(b, den)
            when (bDiv) {
                is Either.Right -> Right(aDiv.getOrElse { 0 } toT bDiv.getOrElse { 0 })
                is Either.Left -> bDiv as Either<String, Nothing>
            }
        }
        is Either.Left -> aDiv as Either<String, Nothing>
    }
}












