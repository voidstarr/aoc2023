import java.io.File

fun LongRange.length() = this.last - this.first

infix fun LongRange.intersect(other: LongRange): LongRange {
    if (first <= other.last && other.first <= last)
        return maxOf(first, other.first).rangeTo(minOf(last, other.last))
    return LongRange(0, 0)
}

infix fun LongRange.overlap(other: LongRange): LongRange {
    
}

fun main() {
    var almanac = File("inputs/day5.1.in").readLines()
    var prevs = almanac[0].split(": ", " ").drop(1).map { it.toLong() }.chunked(2)
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

        while (prevs.zip(map.keys).any { !it.first.intersect(it.second).isEmpty() }) {
            prevs.zip(map.keys).forEach {
                val prev = it.first
                val src = it.second
                val dst = map[src]!!
                val intersection = prev.intersect(src)
                if (!intersection.isEmpty()) {
                    nexts.add(LongRange(dst.first + prev.first - src.first, dst.last + prev.last - src.last))
//                  nexts.add(dst.first + prev - src.first)
                    prevs.remove(it.first)
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