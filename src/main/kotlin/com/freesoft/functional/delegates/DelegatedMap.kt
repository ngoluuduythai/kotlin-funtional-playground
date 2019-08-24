package com.freesoft.functional.delegates

import java.text.SimpleDateFormat
import java.util.*

class DelegatedMap


data class Book(val delegate: Map<String, Any?>) {
    val name: String by delegate
    val authors: String by delegate
    val pageCount: Int by delegate
    val publicationDate: Date by delegate
    val publisher: String by delegate
}


fun main(args: Array<String>) {

    val firstBook = mapOf(
            Pair("name", "Reactive programming"),
            Pair("authors", "Jonas Boner"),
            Pair("pageCount", 400),
            Pair("publicationDate", SimpleDateFormat("yyyy/mm/dd").parse("2017/12/05")),
            Pair("publisher", "O'reilly"))

    val book = Book(firstBook)

    println(book)

}