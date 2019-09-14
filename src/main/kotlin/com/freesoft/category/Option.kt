package com.freesoft.category

sealed class Option<out T> {

    object None : Option<Nothing>() {
        override fun toString() = "None"
    }

    data class Some<out T>(val value: T) : Option<T>()

    companion object
}

fun <T, R> Option<T>.map(transform: (T) -> R): Option<R> = flatMap { t -> Option.Some(transform(t)) }

fun <T, R> Option<T>.flatMap(fm: (T) -> Option<R>): Option<R> = when (this) {
    Option.None -> Option.None
    is Option.Some -> fm(value)
}

fun <A, B, C> ((A) -> B).map(transform: (B) -> C): (A) -> C = { x -> transform(this(x)) }

fun <T, R> List<T>.ap(fab: List<(T) -> R>): List<R> = fab.flatMap { f -> this.map(f) }

fun <T> Option.Companion.pure(t: T) = Option.Some(t)

fun <T, R> Option<T>.ap(fab: Option<(T) -> R>): Option<R> = fab.flatMap { f -> map(f) }

//infix fun <T, R> Option<(T) -> R>.`(*)`(o: Option<T>): Option<R> = flatMap { f: (T) -> R -> o.map(f) }