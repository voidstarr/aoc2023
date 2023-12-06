import java.io.File
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

// I cheated: https://gist.github.com/HexTree/d1a3a36cf794d5cee1d325a63ff26a98
fun mapRanges(inputs: MutableSet<LongRange>, map: HashMap<LongRange, LongRange>): MutableSet<LongRange> {
    val results = mutableSetOf<LongRange>()
    for (input in inputs) {
        var overlaps = mutableSetOf<LongRange>()
        for (src in map.keys) {
            val overlap = input.overlap(src)
            if (!overlap.isEmpty()) {
                val dst = map[src]!!
                results.add(LongRange(overlap.first - src.first + dst.first, overlap.last - src.last + dst.last))
                overlaps.add(overlap)
            }
        }
        if (overlaps.isEmpty()) {
            results.add(input)
            continue
        }

        val sorted = overlaps.toSortedSet(compareBy(LongRange::first, LongRange::last))
        overlaps = sorted

        if (overlaps.first().first > input.first) {
            results.add(LongRange(input.first, overlaps.first().first - 1))
        }

        if (overlaps.last().last < input.last) {
            results.add(LongRange(overlaps.last().last + 1, input.last))
        }

        for (pair in overlaps.chunked(2)) {
            if (pair.size < 2 || pair[1].first <= pair[0].last + 1)
                continue
            results.add(LongRange(pair[1].first + 1, pair[0].last - 1))
        }


    }
    return results
}

fun main() {
    var almanac = File("inputs/day5.1.in").readLines()
    // parse seeds
    val prevs = almanac[0].split(": ", " ").asSequence().drop(1).map { it.toLong() }.chunked(2)
        .map { LongRange(it[0], it[0] + it[1]) }.toMutableSet()
    var nexts = mutableSetOf<LongRange>()
    almanac = almanac.drop(3)

    // parse maps
    val maps = mutableListOf<HashMap<LongRange, LongRange>>()
    var lastLine = almanac.indexOf("")
    while (almanac.isNotEmpty()) {
        val map = HashMap<LongRange, LongRange>()
        for (line in almanac.subList(0, lastLine)) {
            val almanacMap = line.split(' ').map { it.toLong() }
            map[LongRange(almanacMap[1], almanacMap[1] + almanacMap[2] - 1)] =
                LongRange(almanacMap[0], almanacMap[0] + almanacMap[2] - 1)
        }

        maps.add(map)

        almanac = almanac.drop(lastLine + 2)
        lastLine = if (almanac.indexOf("") > 0) almanac.indexOf("") else almanac.size
    }

    // do the thing
    var results = prevs
    for (map in maps) {
        results = mapRanges(results, map)
    }

    println(results.map { it.first }.min())
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