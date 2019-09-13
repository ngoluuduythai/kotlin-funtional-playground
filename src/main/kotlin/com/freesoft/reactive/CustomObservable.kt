package com.freesoft.reactive

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

fun main(args: Array<String>) {

    val observer = object : Observer<String> {
        override fun onComplete() {
            println("all completed")
        }

        override fun onSubscribe(d: Disposable) {
            println("new subscription")
        }

        override fun onNext(t: String) {
            println("Next $t")
        }

        override fun onError(e: Throwable) {
            println("Error occurred => ${e.message}")
        }
    }

    val observable = Observable.create<String> {
        it.onNext("Emitted 1")
        it.onNext("Emitted 2")
        it.onNext("Emitted 3")
        it.onNext("Emitted 4")
        it.onComplete()
    }

    observable.subscribe(observer)

    val secondObservable = Observable.create<String> {
        it.onNext("Emitted 1")
        it.onNext("Emitted 2")
        it.onError(Exception("OHOOOO!!!"))
    }

    secondObservable.subscribe(observer)
}