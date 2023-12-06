import java.io.File
import kotlin.math.ceil
import kotlin.math.sqrt

data class Race(val time: Long, val recordDistance: Long)

fun main() {
    val input = File("inputs/day6.1.in").readLines()
    val times = mutableListOf(input[0].split(" ").drop(1).filter { it != "" }.reduce { acc, s -> acc.plus(s) }.toLong())
    val recordDistances = mutableListOf(input[1].split(" ").drop(1).filter { it != "" }.reduce { acc, s -> acc.plus(s) }.toLong())
    val races = times.zip(recordDistances).map { Race(it.first, it.second) }

    var accumulator = 1
    for (race in races) {
        val discrim = sqrt((race.time * race.time - 4 * race.recordDistance).toDouble())
        if (!discrim.isNaN()) {
            var minButtonTime = ceil(0.5 * (race.time - discrim)).toInt()
            var maxButtonTime = ceil(0.5 * (race.time + discrim)).toInt()
            if (maxButtonTime * (race.time - maxButtonTime) <= race.recordDistance) {
                maxButtonTime -= 1
            }
            if (minButtonTime * (race.time - minButtonTime) <= race.recordDistance) {
                minButtonTime += 1
            }
            val ways = maxButtonTime - minButtonTime + 1
            println(ways)
            accumulator *= ways
        }
    }

    println(accumulator)
}
