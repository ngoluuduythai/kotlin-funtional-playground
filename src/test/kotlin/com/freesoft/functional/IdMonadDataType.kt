package com.freesoft.functional

import arrow.core.Id
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class IdMonadDataType {

    @Test
    fun test_id_monad() {
        val id = Id("123")
        val justId = Id.just("123")

        assertEquals("123", id.extract())
        assertEquals(justId, id)

        val firstMap = id.map { value -> value.length }
                .map { length -> length > 10 }

        val secondMap = id.map { value -> value.length > 10 }

        assertFalse(firstMap.extract())
        assertEquals(firstMap, secondMap)

        val firstFlatMap = id.flatMap { value -> Id.just(value.length) }
                .flatMap { idLength -> Id.just(idLength > 10) }


        val secondFlatMap = id.flatMap { value ->
            Id.just(value.length).flatMap { length -> Id.just(length > 10) }
        }

        assertFalse(firstFlatMap.extract())
        assertEquals(firstFlatMap, secondFlatMap)

    }
}