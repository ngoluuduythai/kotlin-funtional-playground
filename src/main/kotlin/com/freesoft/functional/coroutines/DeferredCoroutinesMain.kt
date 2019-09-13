package com.freesoft.functional.coroutines

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DeferredCoroutinesMain

fun main(args: Array<String>) = runBlocking {
    val result = CompletableDeferred<String>()

    val world = launch {
        delay(500)
        result.complete("World (from another coroutine)")
    }

    val hello = launch {
        println("Hello ${result.await()}")
    }

    hello.join()
    world.join()
}