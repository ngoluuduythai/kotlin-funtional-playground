package com.freesoft.reactive

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable

fun main(args: Array<String>) {

    val list = listOf(1, "Two", 3, "Four", "Five", 5.5f)
    val observable = list.toObservable()

    observable.subscribeBy(
            onNext = { println(it) },
            onError = { it.printStackTrace() },
            onComplete = { println("Done") }
    )


    val observer = object : Observer<Any> {
        override fun onComplete() {
            println("All completed")
        }

        override fun onSubscribe(d: Disposable) {
            println("Subscribed to $d")
        }

        override fun onNext(t: Any) {
            println("Next $t")
        }

        override fun onError(e: Throwable) {
            println("Error occurred $e")
        }
    }


    observable.subscribe(observer)

    val observableOnList = Observable.just(
            listOf("One", 2, "Three", "Four", 4.5, "Five", 6.0f),
            listOf("List with 1 item"),
            listOf(1, 2, 3)
    )

    observableOnList.subscribe(observer)

}