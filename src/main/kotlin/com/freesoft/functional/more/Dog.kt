package com.freesoft.functional.more

class Dog(override val name: String) : Canine(name) {
    override fun speak(): String = "Wof!"
}