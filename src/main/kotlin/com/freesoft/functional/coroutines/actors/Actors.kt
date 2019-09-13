package com.freesoft.functional.coroutines.actors

import com.freesoft.functional.coroutines.repeatInParallel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

sealed class CounterMsg

object IncCounter : CounterMsg()

class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg()

@ObsoleteCoroutinesApi
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0
    for (msg in channel) {
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}


fun main(args: Array<String>) = runBlocking {
    val counterActor = counterActor()

    val time = measureTimeMillis {
        repeatInParallel(1_000_000) {
            counterActor.send(IncCounter)
        }
    }

    val counter = CompletableDeferred<Int>()
    counterActor.send(GetCounter(counter))

    println("counter = ${counter.await()}")
    println("time = $time")
}