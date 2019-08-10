package com.freesoft.functional

class Lambda

/**
 * Function<P1,R> = Function<String,String>
 * (String) -> String
 */
val capitalize = { str: String -> str.capitalize() }

val cap = object : Function1<String, String> {
    override fun invoke(p1: String): String {
        return p1.capitalize()
    }
}


/**
 * Function<P1,P2,R> = Function<String, (String)->String, String>
 *  (String, (String) -> String) -> String
 */
fun transform(str: String, fn: (String) -> String): String {
    return fn(str)
}

fun <T> genericTransform(t: T, fn: (T) -> T): T = fn(t)

fun reverse(argument: String) = argument.reversed()



fun main(args: Array<String>) {
    println(capitalize("hold my beer"))
    println("hold my beer".capitalize())
    println(cap.invoke("aaa"))

    val result = transform("bogdan", capitalize)
    println(result)

    println(genericTransform("kotlin", capitalize))

    println(transform("kotlin", ::reverse))
}

