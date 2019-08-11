package com.freesoft.functional.immutability

import java.lang.Thread.sleep
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync

class SomeData {
    var data: Int = 0
}

fun main(args: Array<String>) {

    val someData = SomeData()

    val firstAsync = supplyAsync {
        for (i in 11..20) {
            someData.data += i
            println("someData from 1st async ${someData.data}")
            sleep(500)
        }
    }

    val secondAsync = supplyAsync {
        for (i in 1..10) {
            someData.data += i
            println("someData from 2nd async ${someData.data}")
            sleep(500)
        }
    }

    CompletableFuture.allOf(firstAsync, secondAsync).get()
}
