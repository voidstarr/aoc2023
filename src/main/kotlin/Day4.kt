import java.io.File
import java.lang.Math.pow

fun day4part1() {
    var accumulator = 0
    File("inputs/day4.1.in").forEachLine { line ->
        val parts = line.split(" | ")
        val winners = parts[0].split(": ")[1].split(' ').filter { it.isNotEmpty() }.toSet()
        val numbersIHave = parts[1].split(' ').filter { it.isNotEmpty() }.toSet()
        val matchingNumbers = numbersIHave.intersect(winners).size
        if (matchingNumbers > 0)
            accumulator += pow(2.0, (matchingNumbers - 1).toDouble()).toInt()

    }
    println(accumulator)
}

fun main() {
    val cards = mutableMapOf<Int, Int>()
    File("inputs/day4.2.in").forEachLine { line ->
        val parts = line.split(" | ")
        val asdf = parts[0].split(": ")[0].split(' ')
        val cardNumber = asdf[asdf.size - 1].toInt()
        cards[cardNumber] = (cards[cardNumber] ?: 0) + 1
        val winners = parts[0].split(": ")[1].split(' ').filter { it.isNotEmpty() }.toSet()
        val numbersIHave = parts[1].split(' ').filter { it.isNotEmpty() }.toSet()
        val matchingNumbers = numbersIHave.intersect(winners).size
        for (i in 1..matchingNumbers) {
            cards[cardNumber + i] = (cards[cardNumber + i] ?: 0) + (cards[cardNumber] ?: 1)
        }
    }
    println(cards.map { it.value }.reduce { acc, i -> acc + i })
}