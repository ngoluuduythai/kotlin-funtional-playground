package com.freesoft.functional.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutinesMain

fun main(args: Array<String>) = runBlocking {
    //    val job = launch {
//        delay(1000)
//        println("World")
//    }
//    print("Hello ")
//    job.join()


    val jobs = List(10_000) {
        launch {
            delay(1000)
            print('.')
        }
    }
    jobs.forEach { job -> job.join() }
}