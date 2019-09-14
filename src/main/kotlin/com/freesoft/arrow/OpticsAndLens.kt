package com.freesoft.arrow

import arrow.optics.Lens


typealias GB = Int

data class Memory(val size: GB)
data class MotherBoard(val brand: String, val memory: Memory)
data class Laptop(val price: Double, val motherBoard: MotherBoard)

val laptopPrice: Lens<Laptop, Double> = Lens(
        get = { laptop -> laptop.price },
        set = { price -> { laptop -> laptop.copy(price = price) } }
)

val laptopMotherBoard: Lens<Laptop, MotherBoard> = Lens(
        get = { laptop -> laptop.motherBoard },
        set = { mb -> { laptop -> laptop.copy(motherBoard = mb) } }
)

val motherBoardMemory: Lens<MotherBoard, Memory> = Lens(
        get = { mb -> mb.memory },
        set = { memory -> { mb -> mb.copy(memory = memory) } }
)

val memorySize: Lens<Memory, GB> = Lens(
        get = { memory -> memory.size },
        set = { size -> { memory -> memory.copy(size = size) } }
)

fun main(args: Array<String>) {
    val laptopX8 = Laptop(500.0, MotherBoard("X", Memory(8)))

    println("Laptop x8 = $laptopX8")

    val laptopX16 = laptopX8.copy(
            price = 780.0,
            motherBoard = laptopX8.motherBoard.copy(
                    memory = laptopX8.motherBoard.memory.copy(
                            size = laptopX8.motherBoard.memory.size * 2
                    )
            )
    )
    println("Laptop x16 = $laptopX16")

    val laptopMemorySize: Lens<Laptop, GB> = laptopMotherBoard compose motherBoardMemory compose memorySize

    val newLaptopX16 = laptopMemorySize.modify(laptopPrice.set(laptopX8, 780.0)) { size -> size * 2 }

    println(newLaptopX16)
}
