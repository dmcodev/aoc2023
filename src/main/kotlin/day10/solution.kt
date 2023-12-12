package day10

import readInputMap

private val vectors = mapOf<Char, Set<Pair<Int, Int>>>(
    '|' to mutableSetOf(0 to -1, 0 to 1),
    '-' to mutableSetOf(-1 to 0, 1 to 0),
    'L' to mutableSetOf(0 to -1, 1 to 0),
    'J' to mutableSetOf(0 to -1, -1 to 0),
    '7' to mutableSetOf(0 to 1, -1 to 0),
    'F' to mutableSetOf(0 to 1, 1 to 0)
)

private val rightSideChecks = mapOf<Triple<Char, Int, Int>, Set<Pair<Int, Int>>>(
    Triple('|', 0, -1) to mutableSetOf(1 to 0),
    Triple('|', 0, 1) to mutableSetOf(-1 to 0),
    Triple('-', 1, 0) to mutableSetOf(0 to 1),
    Triple('-', -1, 0) to mutableSetOf(0 to -1),
    Triple('L', 0, 1) to mutableSetOf(-1 to 0, 0 to 1),
    Triple('L', -1, 0) to mutableSetOf(),
    Triple('J', 1, 0) to mutableSetOf(0 to 1, 1 to 0),
    Triple('J', 0, 1) to mutableSetOf(),
    Triple('7', 1, 0) to mutableSetOf(),
    Triple('7', 0, -1) to mutableSetOf(1 to 0, 0 to -1),
    Triple('F', -1, 0) to mutableSetOf(0 to -1, -1 to 0),
    Triple('F', 0, -1) to mutableSetOf()
)

private fun adjacentPoints(x: Int, y: Int, map: Array<CharArray>): List<Pair<Int, Int>> =
    sequenceOf(x - 1 to y, x to y - 1, x + 1 to y, x to y + 1)
        .filter { it.first >= 0 && it.first < map[0].size && it.second >= 0 && it.second < map.size }
        .toList()

private fun <T> pairs(list: List<T>): List<List<T>> {
    val result = mutableListOf<List<T>>()
    for (i in list.indices) {
        for (j in (i + 1..< list.size)) {
            result.add(listOf(list[i], list[j]))
        }
    }
    return result
}

private fun findStartPosition(map: Array<CharArray>): Pair<Int, Int> {
    val startX = map.indexOfFirst { it.contains('S') }
    val startY = map[startX].indexOfFirst { it == 'S' }
    return startY to startX
}

private fun computeStartingSymbol(position: Pair<Int, Int>, map: Array<CharArray>): Char {
    val matchingAdjacentPair = pairs(adjacentPoints(position.first, position.second, map)).first { adjacentPair ->
        adjacentPair.all { adjacentPoint ->
            vectors[map[adjacentPoint.second][adjacentPoint.first]]
                ?.any { (adjacentPoint.first + it.first to adjacentPoint.second + it.second) == position }
                ?: false
        }
    }
    return vectors.entries
        .first { entry -> entry.value.map { position.first + it.first to position.second + it.second }.toSet() == matchingAdjacentPair.toSet() }
        .key
}

private fun computeMaxDistance(position: Pair<Int, Int>, map: Array<CharArray>): Int {
    val distanceMap = mutableMapOf<Pair<Int, Int>, Int>()
    var queue = mutableListOf<Pair<Int, Int>>()
    queue.add(position)
    var distance = 0
    while (queue.isNotEmpty()) {
        val nextQueue = mutableListOf<Pair<Int, Int>>()
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            distanceMap[point] = distance
            vectors[map[point.second][point.first]]!!
                .map { point.first + it.first to point.second + it.second }
                .filter { !distanceMap.keys.contains(it) }
                .forEach { nextQueue.add(it) }
        }
        distance++
        queue = nextQueue
    }
    return --distance
}

private fun resolvePipePoints(startPosition: Pair<Int, Int>, map: Array<CharArray>): List<Pair<Int, Int>> {
    val points = mutableListOf<Pair<Int, Int>>()
    var position = startPosition
    var prevPosition: Pair<Int, Int>? = null
    do {
        points.add(position)
        val newPosition = vectors[map[position.second][position.first]]!!
            .map { position.first + it.first to position.second + it.second }
            .first { it != prevPosition }
        prevPosition = position
        position = newPosition
    } while (position != startPosition)
    return points
}

private fun expandPoints(entryPoints: List<Pair<Int, Int>>, visited: MutableSet<Pair<Int, Int>>, forbidden: Set<Pair<Int, Int>>, map: Array<CharArray>) {
    val queue = mutableListOf<Pair<Int, Int>>()
    queue.addAll(entryPoints)
    while (queue.isNotEmpty()) {
        val point = queue.removeFirst()
        if (forbidden.contains(point) || visited.contains(point)) {
            continue
        }
        visited.add(point)
        adjacentPoints(point.first, point.second, map)
            .forEach { queue.add(it) }
    }
}

private fun walkPipeWithDirection(startPosition: Pair<Int, Int>, map: Array<CharArray>): List<Int> {
    val pipePoints = resolvePipePoints(startPosition, map).toSet()
    val directions = vectors[map[startPosition.second][startPosition.first]]!!
    val result = mutableListOf<Int>()
    for (direction in directions) {
        val visited = mutableSetOf<Pair<Int, Int>>()
        var position = startPosition
        var vector = direction
        do {
            val nextPosition = position.first + vector.first to position.second + vector.second
            val checkPoints = rightSideChecks[Triple(map[nextPosition.second][nextPosition.first], vector.first, vector.second)]!!
                .map { nextPosition.first + it.first to nextPosition.second + it.second }
            expandPoints(checkPoints, visited, pipePoints, map)
            vector = vectors[map[nextPosition.second][nextPosition.first]]!!
                .filter { nextPosition.first + it.first to nextPosition.second + it.second != position }
                .toList()
                .let {
                    if (it.size != 1) error("")
                    it.first()
                }
            position = nextPosition
        } while (position != startPosition)
        result.add(visited.size)
    }
    return result
}

fun main() {
    val map = readInputMap()
    val startPosition = findStartPosition(map)
    map[startPosition.second][startPosition.first] = computeStartingSymbol(startPosition, map)
    println(computeMaxDistance(startPosition, map))
    println(walkPipeWithDirection(startPosition, map))
}
