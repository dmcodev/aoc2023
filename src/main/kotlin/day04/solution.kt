package day04

import readInputLines
import kotlin.math.pow

private data class Card(val number: Int, val matches: Int) {
    val score = if (matches > 0) 2.toDouble().pow(matches - 1).toInt() else 0
}

private fun parseCards(lines: List<String>): List<Card> =
    lines.mapIndexed { i, line ->
        line.substringAfter(":").split("|")
            .map { it.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }.toSet() }
            .let { lists ->
                val number = line.substringBefore(":").substringAfter("Card").trim().toInt()
                val matches = lists[0].toSet().intersect(lists[1].toSet()).size
                Card(number, matches)
            }
    }

fun main() {
    val cards = parseCards(readInputLines())
    println(cards.sumOf { it.score })
    val deck = mutableMapOf<Int, Int>()
    cards.forEach { deck[it.number] = 1 }
    cards.filter { it.matches > 0 }
        .forEach { card ->
            (card.number + 1 .. card.number + card.matches)
                .forEach { deck[it] = deck[it]!! + deck[card.number]!! }
        }
    println(deck.values.sum())
}
