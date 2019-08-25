package com.freesoft.functional.coroutines

import kotlin.concurrent.thread

class MultithreadsMain

fun main(args: Array<String>) {

    // Not a pretty code. A better solution is near

//    thread {
//        Thread.sleep(1000)
//        println("World")
//    }
//    print("Hello ")
//    Thread.sleep(2000)
    
    val computation = thread {
        Thread.sleep(1000)
        println("World")
    }

    print("Hello ")
    computation.join()
}