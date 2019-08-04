package com.freesoft.functional

import arrow.core.Option
import arrow.core.some
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class OptionDataType {

    @Test
    fun test_option() {
        val factory = Option.just(50)
        val constructor = Option(50)

        val optionSome = 50.some()

        assertEquals(factory, optionSome)
        assertEquals(constructor, optionSome)
        assertNotEquals(factory, 13.some())

        val emptyOption = Option.empty<Int>()
        val fromNullableOption = Option.fromNullable(null)

        assertEquals(emptyOption, fromNullableOption)

        val constructorNullable = Option(null)

        assertNotEquals(fromNullableOption, constructorNullable)
        assertEquals(null.some(), constructorNullable)
    }

    @Test
    fun test_option_map() {
        val constructor: Option<String?> = Option(null)
        val factory: Option<String?> = Option.fromNullable(null)


//        constructor.map { value -> value!!.length } => throws KotlinNullPointerException
//        val map = factory.map { value -> value!!.length } => don't throws KotlinNullPointerException. It returns an Option.None


        try {
            constructor.map { value -> value!!.length }

            Assert.fail()
        } catch (e: KotlinNullPointerException) {
            factory.map { value -> value!!.length }
        }

        assertNotEquals(constructor, factory)
    }
}