package com.freesoft.functional.coroutines.channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext


data class Quote(val value: Double, val client: String, val item: String, val quantity: Int)

data class Bill(val value: Double, val client: String)

data class PickingOrder(val item: String, val quantity: Int)

@ExperimentalCoroutinesApi
fun calculatePriceTransformer(coroutineContext: CoroutineContext,
                              quoteChannel: ReceiveChannel<Quote>
) = CoroutineScope(coroutineContext).produce {
    for (quote in quoteChannel) {
        send(Bill(quote.value * quote.quantity, quote.client) to PickingOrder(quote.item, quote.quantity))
    }
}

@ExperimentalCoroutinesApi
fun cheapBillFilter(
        coroutineContext: CoroutineContext, billChannel: ReceiveChannel<Pair<Bill, PickingOrder>>
) = CoroutineScope(coroutineContext).produce {
    billChannel.consumeEach { (bill, order) ->
        if (bill.value >= 100) {
            send(bill to order)
        } else {
            println("Discarded bill $bill")
        }
    }
}

@ExperimentalCoroutinesApi
suspend fun splitter(
        filteredChannel: ReceiveChannel<Pair<Bill, PickingOrder>>,
        accountingChannel: SendChannel<Bill>,
        warehouseChannel: SendChannel<PickingOrder>
) = CoroutineScope(coroutineContext).launch  {
    filteredChannel.consumeEach { (bill, order) ->
        accountingChannel.send(bill)
        warehouseChannel.send(order)
    }
}

@ExperimentalCoroutinesApi
suspend fun accountingEndpoint(
        accountingChannel: ReceiveChannel<Bill>
) = CoroutineScope(coroutineContext).launch {
    accountingChannel.consumeEach { bill -> println("processing bill: $bill") }
}

@ExperimentalCoroutinesApi
suspend fun warehouseEndpoint(
        warehouseChannel: ReceiveChannel<PickingOrder>
) = CoroutineScope(coroutineContext).launch  {
    warehouseChannel.consumeEach { order -> println("processing order = $order") }
}


@ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking {
    val quoteChannel = Channel<Quote>()
    val accountingChannel = Channel<Bill>()
    val warehouseChannel = Channel<PickingOrder>()

    val transformerChannel = calculatePriceTransformer(coroutineContext, quoteChannel)
    val filteredChannel = cheapBillFilter(coroutineContext, transformerChannel)

    splitter(filteredChannel, accountingChannel, warehouseChannel)

    warehouseEndpoint(warehouseChannel)

    accountingEndpoint(accountingChannel)

    launch(coroutineContext) {
        quoteChannel.send(Quote(20.0, "Foo", "Shoes", 1))
        quoteChannel.send(Quote(20.0, "Bar", "Shoes", 200))
        quoteChannel.send(Quote(200.0, "Foo", "Motorbike", 1))
        quoteChannel.send(Quote(1500.0, "Bar", "Gold", 10))

    }


    delay(1000)
    coroutineContext.cancelChildren()

}