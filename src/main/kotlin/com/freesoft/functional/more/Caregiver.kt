package com.freesoft.functional.more

open class Caregiver(val name: String) {
    open fun Feline.react() = "PURRR"

    fun Canine.react() = "*${this.name} plays with ${this@Caregiver.name}*"

    fun takeCare(feline: Feline) {
        println("Feline reacts: ${feline.react()}")
    }

    fun takeCare(canine: Canine) {
        println("Canine reacts: ${canine.react()}")
    }
}