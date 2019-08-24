package com.freesoft.functional.delegates

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class MakeEvenCustomDelegate(initialValue: Int) : ReadWriteProperty<Any?, Int> {
    private var value: Int = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>) = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, newValue: Int) {
        val oldValue = newValue
        val wasEven = newValue % 2 == 0
        if (wasEven) {
            this.value = newValue
        } else {
            this.value = newValue + 1
        }
        afterAssignmentCall(property, oldValue, newValue, wasEven)
    }

    abstract fun afterAssignmentCall(property: KProperty<*>, oldValue: Int, newValue: Int, wasEven: Boolean): Unit
}

inline fun makeEven(initialValue: Int, crossinline onAssignment: (property: KProperty<*>, oldValue: Int, newValue: Int, wasEven: Boolean) -> Unit): ReadWriteProperty<Any?, Int> = object : MakeEvenCustomDelegate(initialValue) {
    override fun afterAssignmentCall(property: KProperty<*>, oldValue: Int, newValue: Int, wasEven: Boolean) = onAssignment(property, oldValue, newValue, wasEven)
}

var myEven: Int by makeEven(0) { property, oldValue, newValue, wasEven ->
    println("${property.name} $oldValue -> $newValue, Even:$wasEven")
}

fun main(args: Array<String>) {
    myEven = 1
    println(myEven)
    myEven = 3
    println(myEven)
    myEven = 4
    println(myEven)

}