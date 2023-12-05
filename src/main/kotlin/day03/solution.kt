package day03

import adjacentPoints
import readInputMap

private data class Number(val x: Int, val y: Int, val value: Int, val length: Int, val map: Array<CharArray>) {

    val points = (x..< x + length).map { it to y }.toSet()
    val adjacentPoints = points.asSequence()
        .flatMap { adjacentPoints(it.first, it.second, map) }
        .filter { !points.contains(it) }
        .toSet()
    val adjacentStarPoints = adjacentPoints.filter { map[it.second][it.first] == '*' }.toSet()
    val adjacentToAnySymbol = adjacentPoints.any { map[it.second][it.first] != '.' }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Number
        if (x != other.x) return false
        if (y != other.y) return false
        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String = value.toString()
}

private fun parseNumbers(map: Array<CharArray>): List<Number> =
    map.flatMapIndexed { y, line ->
        Regex("\\d+").findAll(String(line)).map {
            Number(it.range.first, y, it.value.toInt(), it.range.run { last - first + 1 }, map)
        }
    }

private fun findGearNumbers(numbers: List<Number>): List<Pair<Number, Number>> =
    numbers.flatMapIndexed { i, first ->
        (i + 1..< numbers.size).asSequence()
            .map { numbers[it] }
            .filter { second -> first.adjacentStarPoints.intersect(second.adjacentStarPoints).isNotEmpty() }
            .map { second -> first to second }
    }

fun main() {
    val numbers = parseNumbers(readInputMap())
    println(numbers.filter { it.adjacentToAnySymbol }.sumOf { it.value })
    println(findGearNumbers(numbers).sumOf { it.first.value * it.second.value })
}
