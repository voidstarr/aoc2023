import java.io.File

val replacements = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

fun replaceAllNumberWords(line: String): String {
    var result = ""
    val buffer = StringBuffer()
    line.forEach { it ->
        if (it.isDigit()) {
            result += it
            buffer.setLength(0)
        } else {
            buffer.append(it)
            if (replacements.keys.any { buffer.contains(it) }) {
                var replacement = replacements[buffer.toString()]
                while (replacement == null) {
                    val newBuff = buffer.drop(1)
                    buffer.setLength(0)
                    buffer.append(newBuff)
                    replacement = replacements[buffer.toString()]
                }
                result += replacements[buffer.toString()]
                val newBuff = buffer.drop(buffer.length - 1)
                buffer.setLength(0)
                buffer.append(newBuff)
            }
        }
    }
    return result
}

fun partone(args: Array<String>) {
    var accumulator = 0
    File("inputs/day1.in").forEachLine {
        val numbers = it.filter { it.isDigit() }
        accumulator += "${numbers[0]}${numbers[numbers.length - 1]}".toInt()
    }
    println(accumulator)
}

fun parttwo(args: Array<String>) {
    var accumulator = 0
    File("inputs/day1.2.in").forEachLine {
        val line: String = replaceAllNumberWords(it)
        val coord = "${line[0]}${line[line.length - 1]}".toInt()
        accumulator += coord
    }
    println(accumulator)
}