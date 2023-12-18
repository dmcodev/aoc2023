package day17

import readInputMap

private enum class Direction(val x: Int, val y: Int) {

    TOP(0, -1),
    RIGHT(1, 0),
    BOTTOM(0, 1),
    LEFT(-1, 0);

    fun left(): Direction = when(this) {
        TOP -> LEFT
        RIGHT -> TOP
        BOTTOM -> RIGHT
        LEFT -> BOTTOM
    }

    fun right(): Direction = when(this) {
        TOP -> RIGHT
        RIGHT -> BOTTOM
        BOTTOM -> LEFT
        LEFT -> TOP
    }
}

private data class Position(val x: Int, val y: Int, val direction: Direction, val length: Int)

private var best = Int.MAX_VALUE
private var sums = mutableMapOf<Position, Int>()

private fun solve(x: Int, y: Int, direction: Direction, length: Int, map: Array<IntArray>, sum: Int) {
    if (x < 0 || x >= map[0].size || y < 0 || y >= map.size || length > 3) {
        return
    }
    val value = map[y][x]
    if (sum + value > best) {
        return
    }
    val position = Position(x, y, direction, length)
    if (!sums.containsKey(position)) {
        sums[position] = sum
    } else {
        if (sum >= sums.getValue(position)) {
            return
        } else {
            sums[position] = sum
        }
    }
    if (x == map[0].size - 1 && y == map.size - 1) {
        if (sum + value < best) {
            best = sum + value
        }
        return
    }
    val right = direction.right()
    val left = direction.left()
    val computeStraight = { solve(x + direction.x, y + direction.y, direction, length + 1, map, sum + value) }
    val computeRight = { solve(x + right.x, y + right.y, right, 1, map, sum + value) }
    val computeLeft = { solve(x + left.x, y + left.y, left, 1, map, sum + value) }
    val partialComputationsOrder = when(direction) {
        Direction.TOP -> listOf(computeRight, computeStraight, computeLeft)
        Direction.RIGHT -> listOf(computeStraight, computeRight, computeLeft)
        Direction.BOTTOM -> listOf(computeStraight, computeLeft, computeRight)
        Direction.LEFT -> listOf(computeLeft, computeStraight, computeRight)
    }
    partialComputationsOrder.forEach { it() }
}

private var bestUltra = Int.MAX_VALUE
private var sumsUltra = mutableMapOf<Position, Int>()

private fun solveUltra(x: Int, y: Int, direction: Direction, length: Int, map: Array<IntArray>, sum: Int) {
    if (x < 0 || x >= map[0].size || y < 0 || y >= map.size || length > 10) {
        return
    }
    val value = map[y][x]
    if (sum + value > bestUltra) {
        return
    }
    val position = Position(x, y, direction, length)
    if (!sumsUltra.containsKey(position)) {
        sumsUltra[position] = sum
    } else {
        if (sum >= sumsUltra.getValue(position)) {
            return
        } else {
            sumsUltra[position] = sum
        }
    }
    if (x == map[0].size - 1 && y == map.size - 1) {
        if (length < 4) {
            return
        }
        if (sum + value < bestUltra) {
            bestUltra = sum + value
        }
        return
    }
    val right = direction.right()
    val left = direction.left()
    val computeStraight = { solveUltra(x + direction.x, y + direction.y, direction, length + 1, map, sum + value) }
    val computeRight = { solveUltra(x + right.x, y + right.y, right, 1, map, sum + value) }
    val computeLeft = { solveUltra(x + left.x, y + left.y, left, 1, map, sum + value) }
    val partialComputationsOrder = if (length >= 4) {
        when(direction) {
            Direction.TOP -> listOf(computeRight, computeStraight, computeLeft)
            Direction.RIGHT -> listOf(computeStraight, computeRight, computeLeft)
            Direction.BOTTOM -> listOf(computeStraight, computeLeft, computeRight)
            Direction.LEFT -> listOf(computeLeft, computeStraight, computeRight)
        }
    } else {
        listOf(computeStraight)
    }
    partialComputationsOrder.forEach { it() }
}

fun main() {
    val map = readInputMap().map { row -> row.map { it.digitToInt() }.toIntArray() }.toTypedArray()
    solve(1, 0, Direction.RIGHT, 1, map, 0)
    solve(0, 1, Direction.BOTTOM, 1, map, 0)
    solveUltra(1, 0, Direction.RIGHT, 1, map, 0)
    solveUltra(0, 1, Direction.BOTTOM, 1, map, 0)
    println(best)
    println(bestUltra)
}
