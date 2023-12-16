package day11

import readInputMap
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private fun solve(multiplier: Long, map: Array<CharArray>) {
    val emptyColumnIndexes = map[0].indices.filter { x -> map.all { it[x] != '#' } }.toSet()
    val emptyRowIndexes = map.indices.filter { y -> map[y].all { it != '#' } }.toSet()
    val galaxies = mutableListOf<Pair<Int, Int>>()
    map.forEachIndexed { y, row ->
        row.forEachIndexed { x, char -> if (char == '#') galaxies.add(x to y) }
    }
    var sum = 0L
    for (i in galaxies.indices) {
        for (j in i + 1 ..< galaxies.size) {
            val firstGalaxy = galaxies[i]
            val secondGalaxy = galaxies[j]
            val xMin = min(firstGalaxy.first, secondGalaxy.first)
            val xMax = max(firstGalaxy.first, secondGalaxy.first)
            val multipliedRowDistance = ((xMin + 1) ..< xMax).count { emptyColumnIndexes.contains(it) } * (multiplier - 1)
            val yMin = min(firstGalaxy.second, secondGalaxy.second)
            val yMax = max(firstGalaxy.second, secondGalaxy.second)
            val multipliedColumnDistance = ((yMin + 1) ..< yMax).count { emptyRowIndexes.contains(it) } * (multiplier - 1)
            sum += multipliedRowDistance + multipliedColumnDistance + abs(xMax - xMin) + abs(yMax - yMin)
        }
    }
    println(sum)
}

fun main() {
    val map = readInputMap()
    solve(2L, map)
    solve(1000000L, map)
}
