import java.io.File

val NUMBERS = "\\d+".toRegex()
val SPECIAL_CHARACTER = "[^A-Za-z0-9\\.]".toRegex()
val GEAR = "\\*".toRegex()

fun day3part2() {
    var accumulator = 0
    val schematic = File("inputs/day3.2.in").readLines()

    val partNumbers = ArrayList<MutableMap<IntRange, Int>>(schematic.size)
    val parts = mutableSetOf<Pair<Int, Int>>()

    for (i in schematic.indices) {
        partNumbers.add(HashMap())
        val numbers = NUMBERS.findAll(schematic[i])
        for (n in numbers)
            partNumbers[i][n.range] = n.value.toInt()

        val partsInLine = GEAR.findAll(schematic[i])
        for (p in partsInLine)
            parts.add(Pair(i, p.range.first))
    }

    for (part in parts) {
        val toAdd = mutableSetOf<Int>()

        for (y in -1..1) {
            val line = partNumbers[part.first + y]
            for (x in -1..1) {
                for (n in line) {
                    if (part.second + x in n.key) {
                        toAdd.add(n.value)
                    }
                }
            }
        }
        if (toAdd.size == 2)
            accumulator += toAdd.reduce { acc, next -> acc * next }
    }
    println(accumulator)
}

fun day3part1() {
    var accumulator = 0
    val schematic = File("inputs/day3.1.in").readLines()

    val partNumbers = ArrayList<MutableMap<IntRange, Int>>(schematic.size)
    val parts = mutableSetOf<Pair<Int, Int>>()

    for (i in schematic.indices) {
        partNumbers.add(HashMap())
        val numbers = NUMBERS.findAll(schematic[i])
        for (n in numbers)
            partNumbers[i][n.range] = n.value.toInt()

        val partsInLine = SPECIAL_CHARACTER.findAll(schematic[i])
        for (p in partsInLine)
            parts.add(Pair(i, p.range.first))
    }

    for (part in parts) {
        val toAdd = mutableSetOf<Int>()

        for (y in -1..1) {
            val line = partNumbers[part.first + y]
            for (x in -1..1) {
                for (n in line) {
                    if (part.second + x in n.key) {
                        toAdd.add(n.value)
                    }
                }
            }
        }

        toAdd.forEach { accumulator += it }
    }
    println(accumulator)
}