package com.freesoft.functional.pastry

interface Oven : Machine<Bakeable> {
    override fun process(product: Bakeable)
}