package com.freesoft.functional.more

fun String.sendToConsole() = println(this)
fun printSpeak(canine: Canine) {
    println(canine.speak())
}

fun printFelineSpeak(feline: Feline) {
    println(feline.speak())
}

fun Feline.speak() = "mew"

fun Cat.speak() = "cat mew"

fun main(args: Array<String>) {

    "Hello world".sendToConsole()

    printSpeak(Dog("berkley"))
    printSpeak(Canine("berkley"))


    printFelineSpeak(Feline())
    printFelineSpeak(Cat())

    val noah = Caregiver("Noah")
    val tom = Cat()

    val barkley = Canine("Barkley")

    noah.takeCare(tom)
    noah.takeCare(barkley)
}