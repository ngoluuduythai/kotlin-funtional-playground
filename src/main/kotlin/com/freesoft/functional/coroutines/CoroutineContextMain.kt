package com.freesoft.functional.coroutines

import kotlinx.coroutines.*

class CoroutineContextMain

fun main(args: Array<String>) = runBlocking {
    println("run blocking coroutineContext = $coroutineContext")
    println("coroutineContext[Job]= ${coroutineContext[Job]}")
    println(Thread.currentThread().name)
    println("----------------------------------")

    val jobs = listOf(
            launch {
                println("LAUNCH")
                println("launch coroutineContext = $coroutineContext")
                println("coroutineContext[Job]= ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
            },
            async {
                println("ASYNC")
                println("async coroutineContext = $coroutineContext")
                println("coroutineContext[Job]= ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
            },
            launch(Dispatchers.Default) {
                println("Dispatchers Default")
                println("dispatchers default = $coroutineContext")
                println("coroutineContext[Job]= ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
            },
            launch(coroutineContext) {
                println("Coroutine context")
                println("coroutineContext = $coroutineContext")
                println("coroutineContext[Job]= ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
            }
    )

    jobs.forEach { job ->
        println("job = $job")
        job.join()
    }
}