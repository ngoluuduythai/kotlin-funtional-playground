package com.freesoft.arrow

import arrow.syntax.function.andThen
import arrow.syntax.function.forwardCompose

data class Quote(val value: Double, val client: String, val item: String, val quantity: Int)

data class Bill(val value: Double, val client: String)

data class PickingOrder(val item: String, val quantity: Int)

fun calculatePrice(quote: Quote) = Bill(quote.value * quote.quantity, quote.client) to PickingOrder(quote.item, quote.quantity)

fun filterBills(billAndOrder: Pair<Bill, PickingOrder>): Pair<Bill, PickingOrder>? {
    val (bill, _) = billAndOrder
    return if (bill.value >= 100) {
        billAndOrder
    } else {
        null
    }
}

fun warehouse(order: PickingOrder) {
    println("processing order = $order")
}

fun accounting(bill: Bill) {
    println("processing = $bill")
}

fun splitter(billAndOrder: Pair<Bill, PickingOrder>?) {
    if (billAndOrder != null) {
        warehouse(billAndOrder.second)
        accounting(billAndOrder.first)
    }
}

fun main(args: Array<String>) {
    val salesSystem: (Quote) -> Unit = ::calculatePrice andThen ::filterBills forwardCompose ::splitter
    salesSystem(Quote(20.0, "foo", "shoes", 1))
    salesSystem(Quote(20.0, "bar", "shoes", 200))
    salesSystem(Quote(2000.0, "foo", "motorbike", 1))
}
