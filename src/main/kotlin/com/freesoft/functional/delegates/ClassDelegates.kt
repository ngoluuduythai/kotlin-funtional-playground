package com.freesoft.functional.delegates

interface Person {
    fun printName()
}

class PersonImpl(val name: String) : Person {
    override fun printName() {
        println(name)
    }
}

class User(val person: Person) : Person by person {
    override fun printName() {
        println("Printing Name:")
        person.printName()
    }
}

fun main(args: Array<String>) {
    val person = PersonImpl("Duke Duke")
    person.printName()
    println()
    val user = User(person)
    user.printName()
}