package com.freesoft.functional.more

class Wolf(val name: String) {
    operator fun plus(wolf: Wolf) = Pack(mapOf(name to this, wolf.name to wolf))
}

class Pack(val members: Map<String, Wolf>)

fun main(args: Array<String>) {

    val talbot = Wolf("Talbot")
    val northPack: Pack = talbot + Wolf("Big Bertha")

}