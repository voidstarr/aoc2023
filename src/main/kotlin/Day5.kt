import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

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

fun <T, U> Collection<T>.cartesianProduct(c2: Collection<U>): List<Pair<T, U>> {
    return flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
}

fun main() {
    var almanac = File("inputs/day5.example.in").readLines()
    var prevs = almanac[0].split(": ", " ").asSequence().drop(1).map { it.toLong() }.chunked(2)
        .map { LongRange(it[0], it[0] + it[1]) }.toMutableSet()
    var nexts = mutableSetOf<LongRange>()

    almanac = almanac.drop(3)

    var lastLine = almanac.indexOf("")
    while (almanac.isNotEmpty()) {
        val map = HashMap<LongRange, LongRange>()

        for (line in almanac.subList(0, lastLine)) {
            val almanacMap = line.split(' ').map { it.toLong() }
            map[LongRange(almanacMap[1], almanacMap[1] + almanacMap[2] - 1)] =
                LongRange(almanacMap[0], almanacMap[0] + almanacMap[2] - 1)
        }

        var overhangs = mutableSetOf<LongRange>()
        while (prevs.isNotEmpty()) {
            val prev = prevs.first()
            println("prevs: $prevs srcs: ${map.keys} overhangs: $overhangs")
            var isOverlap = false
            for (src in map.keys) {
                val overlap = prev.overlap(src)
                isOverlap = !overlap.isEmpty()
                if (isOverlap) {
                    prevs.remove(prev)

                    val dst = map[src]!!
                    val overlapDst =
                        LongRange(overlap.first - src.first + dst.first, overlap.last - src.last + dst.last)
                    nexts.add(overlapDst)

                    println("src: $src, dst: $dst")
                    println("overlap: $overlap between $prev and $dst")

                    val leftOverhangStart = min(prev.first, src.first)
                    if (leftOverhangStart < overlap.first) {
                        val overhang = LongRange(leftOverhangStart, overlap.first - 1)
                        overhangs.add(overhang)
                        println("left overhang: $overhang")
                    }

                    val rightOverhangEnd = max(prev.last, src.last)
                    if (rightOverhangEnd > overlap.last) {
                        val overhang = LongRange(overlap.last + 1, rightOverhangEnd)
                        overhangs.add(overhang)
                        println("left overhang: $overhang")
                    }
                }
            }
            if (!isOverlap) {
                overhangs.add(prev)
                prevs.remove(prev)
            }
            overhangs = overhangs.filter { !it.isEmpty() }.toMutableSet()
            if (prevs.size == 0) prevs.addAll(overhangs)
            println("$prevs ${map.keys}")
        }

        nexts.addAll(overhangs)

        prevs = nexts
        nexts = mutableSetOf()

        almanac = almanac.drop(lastLine + 2)
        lastLine = if (almanac.indexOf("") > 0) almanac.indexOf("") else almanac.size
    }
    println(prevs.map { it.first }.min())
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