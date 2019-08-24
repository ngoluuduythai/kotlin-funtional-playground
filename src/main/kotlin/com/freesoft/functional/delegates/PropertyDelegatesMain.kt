package com.freesoft.functional.delegates

import kotlin.properties.Delegates

class PropertyDelegatesMain

var notNullString: String by Delegates.notNull<String>()
lateinit var anotherNotNullString: String

val myLazyVal: String by lazy {
    println("Just initialised")
    "My lazy intialization"
}

var myString: String by Delegates.observable("<Initial Value>") { property, oldValue, newValue ->
    println("Property `${property.name}` changed value from $oldValue to $newValue")
}

fun main(args: Array<String>) {

    notNullString = "Something"
    anotherNotNullString = "Other thing"
    println(notNullString)
    println(anotherNotNullString)

    println(myLazyVal)

    myString = "Change value"
    myString = "Change value again"
}