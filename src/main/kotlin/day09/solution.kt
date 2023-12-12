package day09

import readInputLines

private fun diff(input: List<Int>): List<Int> =
    input.windowed(2, 1).map { it[1] - it[0] }

private fun analyze(input: List<Int>): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    result.add(input)
    var current = input
    while (!current.all { it == 0 }) {
        current = diff(current)
        result.add(current)
    }
    return result
}

private fun extrapolate(input: List<Int>): Int =
    analyze(input).map { it.last() }.sum()

private fun extrapolateBackwards(input: List<Int>): Int =
    analyze(input).map { it.first() }.reversed().reduce { diff, number -> number - diff }

fun main() {
    val sequences = readInputLines().map { it.split(" ").map { it.toInt() } }
    println(sequences.map { extrapolate(it) }.sum())
    println(sequences.map { extrapolateBackwards(it) }.sum())
}
