package com.freesoft.arrow

import arrow.syntax.function.*

fun partialSplitter(
        billAndOrder: Pair<Bill, PickingOrder>?,
        warehouse: (PickingOrder) -> Unit,
        accounting: (Bill) -> Unit) {
    if (billAndOrder != null) {
        warehouse(billAndOrder.second)
        accounting(billAndOrder.first)
    }
}

fun main(args: Array<String>) {
    val strong: (String, String, String) -> (String) = { body, id, style ->
        "<strong id=\"$id\" style=\"$style\">$body</strong>"
    }

    val redStrong: (String, String) -> String = strong.partially3("font: red") // explicit partial application

    val blueStrong: (String, String) -> String = strong.invoke(p3 = "font: blue") // implicit partial application


    println(redStrong("Red Sonja", "movie1"))
    println(blueStrong("Deep blue sea", "movie2"))


    val splitter: (billAndOrder: Pair<Bill, PickingOrder>?) -> Unit = ::partialSplitter.partially2 { order -> println("Testing $order") }(p2 = ::accounting)

    val salesSystem: (quote: Quote) -> Unit = ::calculatePrice.andThen(::filterBills).forwardCompose(splitter)

    salesSystem(Quote(20.0, "Foo", "Shoes", 1))
    salesSystem(Quote(20.0, "Bar", "Shoes", 200))
    salesSystem(Quote(2000.0, "Foo", "Motorbike", 1))

}