package com.freesoft.functional.firstclasshigherorder

fun unless(condition: Boolean, block: () -> Unit) {
    if (!condition) block()
}

fun main(args: Array<String>) {
    val securityCheck = false

    unless(securityCheck) {
        println("can't access this website")
    }
}
