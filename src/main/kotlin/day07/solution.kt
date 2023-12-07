package day07

import readInputLines
import kotlin.math.min

private val cardsStrength = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    .reversed()
    .mapIndexed { index, char -> char to index }
    .toMap()
private val cardsStrengthWithJokers = cardsStrength.toMutableMap().apply { put('J', -1) }

private val fiveCount = listOf(5)
private val fourCount = listOf(4, 1)
private val fullHouseCount = listOf(3, 2)
private val threeCount = listOf(3, 1, 1)
private val twoPairCount = listOf(2, 2, 1)
private val onePairCount = listOf(2, 1, 1, 1)
private val highCardCount = listOf(1, 1, 1, 1, 1)

private val countsStrength = listOf(highCardCount, onePairCount, twoPairCount, threeCount, fullHouseCount, fourCount, fiveCount)

private data class Hand(val value: String, val bid: Int, val jokers: Boolean = false): Comparable<Hand> {
    private val count = value.toCharArray().asSequence().groupingBy { it }.eachCount().values.toList().sortedDescending()
    private val jokersCount = value.toCharArray().count { it == 'J' }
    private val strength = if (jokers && jokersCount > 0) {
        val countWithJokers = count.toMutableList().apply { remove(jokersCount) }
        var jokersToDistribute = jokersCount
        countWithJokers.indices.forEach {
            val maxAddition = 5 - countWithJokers[it]
            val addition = min(maxAddition, jokersToDistribute)
            countWithJokers[it] += addition
            jokersToDistribute -= addition
        }
        if (jokersToDistribute > 0) {
            countWithJokers.add(jokersToDistribute)
        }
        countsStrength.indexOf(countWithJokers)
    } else {
        countsStrength.indexOf(count)
    }
    override fun compareTo(other: Hand): Int {
        val cardsComparison = if (jokers) cardsStrengthWithJokers else cardsStrength
        return when {
            strength > other.strength -> 1
            strength < other.strength -> -1
            else -> value.indices.asSequence()
                .map { cardsComparison.getValue(value[it]).compareTo(cardsComparison.getValue(other.value[it])) }
                .filter { it != 0 }
                .firstOrNull() ?: 0
        }
    }
}

fun main() {
    val hands = readInputLines().map { it.split(" ").let { Hand(it[0], it[1].toInt()) } }
    val handsWithJokers = readInputLines().map { it.split(" ").let { Hand(it[0], it[1].toInt(), true) } }
    println(hands.sorted().mapIndexed { index, hand -> hand.bid * (index + 1L) }.sum())
    println(handsWithJokers.sorted().mapIndexed { index, hand -> hand.bid * (index + 1L) }.sum())
}
