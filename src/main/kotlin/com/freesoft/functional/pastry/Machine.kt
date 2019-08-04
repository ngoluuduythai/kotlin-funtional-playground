package com.freesoft.functional.pastry

interface Machine<T> {
    fun process(product: T)
}