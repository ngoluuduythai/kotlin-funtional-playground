package com.freesoft.functional.coroutines

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    var counter = 0

    val counterContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    val time = measureTimeMillis {
        repeatInParallel(1_000_000) {
            withContext(counterContext) {
                counter++
            }
        }
    }

    println("counter = $counter")
    println("time = $time")
}