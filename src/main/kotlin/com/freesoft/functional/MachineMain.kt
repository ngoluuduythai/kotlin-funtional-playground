package com.freesoft.functional

import com.freesoft.functional.pastry.Machine

fun <T> useMachine(t: T, machine: Machine<T>) {
    machine.process(t)
}

class PrintMachine<T> : Machine<T> {
    override fun process(product: T) {
        println(product)
    }
}

typealias Machine<T> = (T) -> Unit



fun main(args: Array<String>) {
    useMachine(5, PrintMachine())

    useMachine(5, object : Machine<Int> {
        override fun process(product: Int) {
            println(product)
        }
    })
}