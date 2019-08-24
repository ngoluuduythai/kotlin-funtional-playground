package com.freesoft.functional.delegates

import kotlin.properties.Delegates

class VetoableDelegates

var myIntEven: Int by Delegates.vetoable(0) { property, oldValue, newValue ->
    println("${property.name} $oldValue -> $newValue")
    newValue % 2 == 0
}


fun main(args: Array<String>) {

    println(myIntEven)
    myIntEven = 2
    println(myIntEven)
    myIntEven = 5
    println(myIntEven)
    myIntEven = 3
    println(myIntEven)
    myIntEven = 4
    println(myIntEven)

}