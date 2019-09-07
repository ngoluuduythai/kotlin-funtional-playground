package com.freesoft.functional.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis

suspend fun repeatInParallel(times: Int, block: suspend () -> Unit) {
    val job = CoroutineScope(coroutineContext).launch {
        repeat(times) {
            launch(coroutineContext) {
                block()
            }
        }
    }
    job.join()
}

fun main(args: Array<String>) = runBlocking {
    var counter = 0
    val time = measureTimeMillis {
        repeatInParallel(1_000_000_000) {
            counter++
        }
    }

    println("counter = $counter")
    println("time = $time")
}