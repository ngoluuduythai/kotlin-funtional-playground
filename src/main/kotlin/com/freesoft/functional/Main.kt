package com.freesoft.functional

import com.freesoft.functional.pastry.Bakeable
import com.freesoft.functional.pastry.Machine
import com.freesoft.functional.pastry.Person

class Main

typealias Oven = Machine<Bakeable>
typealias Flavour = String

fun main(args: Array<String>) {

    val person = Person(1, "Foo Bar", 24)
    val (id, name, age) = person

    val idComponent = person.component1()
    val nameComponent = person.component2()
    val ageComponent = person.component3()

    println(id)
    println(idComponent)

    val sum = { x: Int, y: Int -> x + y }
    println("Sum = ${sum(10, 20)}")

}