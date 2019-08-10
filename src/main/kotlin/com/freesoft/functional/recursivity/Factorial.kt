package com.freesoft.functional.recursivity

fun imperativeFactorial(n: Long): Long {
    var result = 1L
    for (i in 1..n) {
        result *= i
    }
    return result
}

fun recursiveFactorial(n: Long): Long {
    fun go(n: Long, acc: Long): Long {
        return if (n <= 0) {
            acc
        } else {
            go(n - 1, n * acc)
        }
    }
    return go(n, 1)
}

fun tailrecRecursiveFactorial(n: Long): Long {
    tailrec fun go(n: Long, acc: Long): Long {
        return if (n <= 0) {
            acc
        } else {
            go(n - 1, n * acc)
        }
    }
    return go(n, 1)
}

fun executionTime(body: () -> Unit): Long {
    val startTime = System.nanoTime()
    body()
    val endTime = System.nanoTime()
    return endTime - startTime
}

fun main(args: Array<String>) {
//    val imperativeFactorial = imperativeFactorial(10)
//    println(imperativeFactorial)
//    val recursiveFactorial = recursiveFactorial(10)
//    println(recursiveFactorial)

    val imperativeFactorialTime = executionTime { imperativeFactorial(100) }
    val recursiveFactorialTime = executionTime { recursiveFactorial(100) }
    val tailrecFactorialTime = executionTime { tailrecRecursiveFactorial(100) }

    println("Imperative factorial: $imperativeFactorialTime Recursive time: $recursiveFactorialTime and tailrecTime: $tailrecFactorialTime")
}