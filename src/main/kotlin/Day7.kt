import java.io.File

fun cardVal(c: Char): Int {
    return if (c in '2'..'9') c.digitToInt()
    else when (c) {
        'T' -> 10
        'J' -> 1
        'Q' -> 12
        'K' -> 13
        'A' -> 14
        else -> throw IllegalArgumentException()
    }
}

fun handType(hand: String): Hand.HandType {
    var groupedHand = hand.groupBy { it }.toMutableMap()

    if (hand.contains('J') && groupedHand.size > 1) {
        groupedHand.remove('J')
        val mostCards = groupedHand.maxWith { a, b -> a.value.size.compareTo(b.value.size) }
        groupedHand.put(mostCards.key, MutableList(mostCards.value.size + hand.count { it == 'J' }) { mostCards.key }.toList())
    }

    return if (groupedHand.size == 1) {
        Hand.HandType.FIVE_OF_A_KIND
    } else if (groupedHand.any { it.value.size == 4 }) {
        Hand.HandType.FOUR_OF_A_KIND
    } else if (groupedHand.size == 2 && groupedHand.any { it.value.size == 3 }) {
        Hand.HandType.FULL_HOUSE
    } else if (groupedHand.size > 2 && groupedHand.any { it.value.size == 3 }) {
        Hand.HandType.TRIPS
    } else if (groupedHand.filter { it.value.size == 2 }.size == 2) {
        Hand.HandType.TWO_PAIR
    } else if (groupedHand.filter { it.value.size == 2 }.size == 1) {
        Hand.HandType.ONE_PAIR
    } else {
        Hand.HandType.HIGH_CARD
    }
}

data class Hand(val cards: String, val wager: Int, val type: HandType = handType(cards)) : Comparable<Hand> {

    enum class HandType {
        FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, TRIPS, TWO_PAIR, ONE_PAIR, HIGH_CARD
    }


    fun value(idx: Int): Int {
        return (idx + 1) * wager
    }

    override fun compareTo(other: Hand): Int {
        if (type != other.type)
            return other.type.ordinal - type.ordinal
        for (i in cards.indices) {
            if (cards[i] != other.cards[i])
                return cardVal(cards[i]) - cardVal(other.cards[i])
        }
        return 0
    }
}

fun main() {
    val rankedHands = File("inputs/day7.1.in").readLines().filter { it != "" }.map {
        val parts = it.split(" ")
        Hand(parts[0], parts[1].toInt())
    }.sorted()

    rankedHands.mapIndexed { idx, it -> "${idx + 1}: $it value=${it.value(idx)}" }.filter { it.contains('J') }
        .forEach { println(it) }

    println(rankedHands.foldIndexed(0) { idx, acc, it -> acc + it.value(idx) })

}
