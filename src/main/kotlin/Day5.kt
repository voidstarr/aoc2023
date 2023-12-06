import java.io.File
import kotlin.math.max
import kotlin.math.min

fun LongRange.length() = this.last - this.first

// https://stackoverflow.com/questions/36035074/how-can-i-find-an-overlap-between-two-given-ranges
infix fun LongRange.overlap(other: LongRange): LongRange {
    val newFirst = max(first, other.first)
    val newLast = min(last, other.last)
    return if (newFirst <= newLast) {
        LongRange(newFirst, newLast)
    } else {
        LongRange(0, -1)
    }
}

fun main() {
    var almanac = File("inputs/day5.example.in").readLines()
    var prevs = almanac[0].split(": ", " ").asSequence().drop(1).map { it.toLong() }.chunked(2)
        .map { LongRange(it[0], it[0] + it[1]) }.toMutableList()
    var nexts = ArrayList<LongRange>()

    almanac = almanac.drop(3)

    var lastLine = almanac.indexOf("")
    while (almanac.isNotEmpty()) {
        val map = HashMap<LongRange, LongRange>()

        for (line in almanac.subList(0, lastLine)) {
            val almanacMap = line.split(' ').map { it.toLong() }
            map[LongRange(almanacMap[1], almanacMap[1] + almanacMap[2] - 1)] =
                LongRange(almanacMap[0], almanacMap[0] + almanacMap[2] - 1)
        }

        while (prevs.zip(map.keys).any { !it.first.overlap(it.second).isEmpty() }) {
            for (pair in prevs.zip(map.keys)) {
                val prev = pair.first
                val src = pair.second
                val dst = map[src]!!
                val overlap = prev.overlap(src)
                if (!overlap.isEmpty()) {
                    nexts.add(LongRange(overlap.first - src.first + dst.first, overlap.last - src.last + dst.last))
                    // TODO: add back residual ranges to prevs

                    prevs.remove(pair.first)
                }
            }
        }

        nexts.addAll(prevs)

        prevs = nexts
        nexts = ArrayList()

        almanac = almanac.drop(lastLine + 2)
        lastLine = if (almanac.indexOf("") > 0) almanac.indexOf("") else almanac.size
    }
    println(prevs)
}

fun partone() {
    var almanac = File("inputs/day5.1.in").readLines()
    var seeds = almanac[0].split(": ", " ").drop(1).map { it.toLong() }.toMutableList()
    var nexts = ArrayList<Long>()

    almanac = almanac.drop(3)

    var lastLine = almanac.indexOf("")
    while (almanac.isNotEmpty()) {
        val map = HashMap<LongRange, LongRange>()

        for (line in almanac.subList(0, lastLine)) {
            val almanacMap = line.split(' ').map { it.toLong() }
            map[LongRange(almanacMap[1], almanacMap[1] + almanacMap[2] - 1)] =
                LongRange(almanacMap[0], almanacMap[0] + almanacMap[2] - 1)
        }

        for (prev in seeds) {
            var outOfRange = true
            for (range in map.keys) {
                if (prev in range) {
                    nexts.add(map[range]!!.first + prev - range.first)
                    outOfRange = false
                    break
                }
            }
            if (outOfRange) {
                nexts.add(prev)
            }
        }
        seeds = nexts
        nexts = ArrayList()

        almanac = almanac.drop(lastLine + 2)
        lastLine = if (almanac.indexOf("") > 0) almanac.indexOf("") else almanac.size
    }
    println(seeds.min())
}