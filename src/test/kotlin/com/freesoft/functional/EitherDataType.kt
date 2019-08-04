package com.freesoft.functional

import arrow.core.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EitherDataType {

    @Test
    fun either_test() {

        val rightOnly: Either<String, Int> = Either.right(42)
        val leftOnly: Either<String, Int> = Either.left("foo")

        assertTrue(rightOnly.isRight())
        assertTrue(leftOnly.isLeft())

        assertEquals(42, rightOnly.getOrElse { -1 })
        assertEquals(-1, leftOnly.getOrElse { -1 })
    }

    @Test
    fun test_either_map() {
        val rightOnly: Either<String, Int> = Either.right(50)
        val leftOnly: Either<String, Int> = Either.left("bar")

        println(rightOnly.left())
        println(leftOnly.right())

        assertEquals(0, rightOnly.map { it % 2 }.getOrElse { -1 })
        assertEquals(-1, leftOnly.map { it % 2 }.getOrElse { -1 })

        val rightFlatMap = rightOnly.flatMap { Either.right(it % 2) }
        val leftFlatMap = leftOnly.flatMap {
            println(it)
            Either.Right(it % 2)
        }

        assertTrue(rightFlatMap.isRight())
        assertTrue(leftFlatMap.isLeft())
    }
}