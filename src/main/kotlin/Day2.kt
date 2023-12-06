import java.io.File

fun main() {
    var accumulator = 0
    File("inputs/day2.2.in").forEachLine { game ->
        val parts = game.split(": ")
        val gameId = parts[0].split(' ')[1].toInt()
        run game@{
            val minColors = mutableMapOf(
                "red" to 0,
                "blue" to 0,
                "green" to 0
            )
            parts[1].split("; ").forEach { round ->
                round.split(", ").forEach {
                    val cubes = it.split(' ')
                    if (cubes[0].toInt() > (minColors[cubes[1]] ?: Int.MIN_VALUE)) {
                        minColors.set(cubes[1], cubes[0].toInt())
                    }
                }
            }
            accumulator += minColors.map { it.value }.reduce { acc, i -> acc * i }
        }
    }
    println(accumulator)
}

fun part1() {
    val limits = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )

    var accumulator = 0
    File("inputs/day2.1.in").forEachLine { game ->
        val parts = game.split(": ")
        val gameId = parts[0].split(' ')[1].toInt()
        run game@{
            parts[1].split("; ").forEach { round ->
                round.split(", ").forEach {
                    val cubes = it.split(' ')
                    if (cubes[0].toInt() > (limits[cubes[1]] ?: Int.MAX_VALUE)) {
                        return@game
                    }
                }
            }
            accumulator += gameId
        }
    }
    println(accumulator)
}