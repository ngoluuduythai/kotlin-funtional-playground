package com.freesoft.functional.coroutines

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChannelMain

fun main(args: Array<String>) = runBlocking {


    val numbers = listOf(1, 2, 3, 4, 5)

    val channel = Channel<String>()
    val channelNumber = Channel<Int>()


    val sendNumbers = launch {
        numbers.forEach { number ->
            delay(500)
            channelNumber.send(number)
        }
    }

    repeat(5) {
        println("Receive ${channelNumber.receive()}")
    }
//
//    val receiveNumbers = launch {
//        println("Receive ${channelNumber.receive()}")
//    }

//    val world = launch {
//        delay(500)
//        channel.send("World (from another coroutine using a channel)")
//    }
//    val hello = launch {
//        println("Hello ${channel.receive()}")
//    }

    sendNumbers.join()
//    receiveNumbers.join()

//    hello.join()
//    world.join()
}