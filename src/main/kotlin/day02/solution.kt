package day02

import readInputLines

private data class Game(val id: Int, val reveals: List<CubesReveal>) {

    fun minimalBag(): Map<String, Int> =
        reveals.flatMap { it.minimalBag().entries }
            .groupingBy { it.key }
            .reduce { _, left, right -> if (left.value >= right.value) left else right }
            .mapValues { it.value.value }
}

private data class CubesReveal(val cubes: List<CubeReveal>) {

    fun isValidForBag(bag: Map<String, Int>): Boolean =
        cubes.all { bag.getOrDefault(it.type, Int.MAX_VALUE) >= it.size }

    fun minimalBag(): Map<String, Int> =
        cubes.groupingBy { it.type }
            .reduce { _, left, right -> if (left.size >= right.size) left else right }
            .mapValues { it.value.size }
}

private data class CubeReveal(val type: String, val size: Int)

private fun parseGames(lines: List<String>): List<Game> = lines.map { line ->
    val id = line.split(":")[0].substringAfter("Game ").toInt()
    val reveals = line.split(":")[1].trim().split(";")
        .map { CubesReveal(it.split(",").map { it.trim().split(" ").let { CubeReveal(it[1], it[0].toInt()) } }) }
    Game(id, reveals)
}

fun main() {
    println(
        parseGames(readInputLines())
            .filter { it.reveals.all { it.isValidForBag(mapOf("red" to 12, "green" to 13, "blue" to 14)) } }
            .sumOf { it.id }
    )
    println(
        parseGames(readInputLines())
            .sumOf { it.minimalBag().values.reduce { left, right -> left * right } }
    )
}