package com.freesoft.functional.collections

sealed class FuncList<out T> {

    object Nil : FuncList<Nothing>()

    data class Cons<out T>(val head: T, val tail: FuncList<T>) : FuncList<T>()

    fun forEach(f: (T) -> Unit) {
        tailrec fun go(list: FuncList<T>, f: (T) -> Unit) {
            when (list) {
                is Cons -> {
                    f(list.head)
                    go(list.tail, f)
                }
                is Nil -> Unit
            }
        }
        go(this, f)
    }

    fun <R> fold(init: R, f: (R, T) -> R): R {
        tailrec fun go(list: FuncList<T>, init: R, f: (R, T) -> R): R = when (list) {
            is Cons -> go(list = list.tail, init = f(init, list.head), f = f)
            is Nil -> init
        }
        return go(this, init, f)
    }

    fun reverse(): FuncList<T> = fold(Nil as FuncList<T>) { acc, i -> Cons(i, acc) }

    fun <R> foldRight(init: R, f: (R, T) -> R): R {
        return this.reverse().fold(init, f)
    }

    fun <R> map(f: (T) -> R): FuncList<R> {
        return foldRight(Nil as FuncList<R>) { tail, head -> Cons(f(head), tail) }
    }

    fun <T, S> unfold(s: S, f: (S) -> Pair<T, S>?): Sequence<T> {
        val result = f(s)
        return if (result != null) {
            sequenceOf(result.first) + unfold(result.second, f)
        } else {
            sequenceOf()
        }
    }

    fun <T> elements(element: T, numOfValues: Int): Sequence<T> {
        return unfold(1) { i ->
            if (numOfValues > i)
                element to i + 1
            else
                null
        }
    }

    fun corecursiveFactorial(size: Int): Sequence<Long> {
        return sequenceOf(1L) + unfold(1L to 1) { (acc, n) ->
            println("n: $n , acc: $acc")
            if (size > n) {
                val x = n * acc
                (x) to (x to n + 1)
            } else
                null
        }
    }
}