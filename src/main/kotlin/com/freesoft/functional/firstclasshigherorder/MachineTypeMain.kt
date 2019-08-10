package com.freesoft.functional

typealias Processor<T> = (T) -> Unit

fun <T> useProcessor(t: T, machine: Processor<T>) {
    machine(t)
}

class PrintProcessor<T> : Processor<T> {
    override fun invoke(p1: T) {
        println(p1)
    }
}

fun main(args: Array<String>) {
    useProcessor(10, PrintProcessor())
}