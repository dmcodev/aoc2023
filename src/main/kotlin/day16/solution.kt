package day16

import day16.MoveResult.Continues
import readInputMap
import kotlin.math.max

private enum class Direction(val x: Int, val y: Int) {

    TOP(0, -1),
    RIGHT(1, 0),
    BOTTOM(0, 1),
    LEFT(-1, 0);

    fun applyTo(beam: Beam): Beam =
        Beam(beam.x + x, beam.y + y, this)
}

private sealed class MoveResult {

    data class Continues(val beam: Beam) : MoveResult()

    data object Faded : MoveResult()

    data class Splits(val beams: List<Beam>) : MoveResult()
}

private data class Beam(val x: Int, val y: Int, val direction: Direction) {

    fun move(map: Array<CharArray>, beamTraces: Set<Beam>): MoveResult {
        if (beamTraces.contains(this) || x < 0 || x >= map[0].size || y < 0 || y >= map.size) {
            return MoveResult.Faded
        }
        val token = map[y][x]
        return when {
            token == '.' -> Continues(direction.applyTo(this))
            token == '-' && direction == Direction.RIGHT -> Continues(direction.applyTo(this))
            token == '-' && direction == Direction.LEFT -> Continues(direction.applyTo(this))
            token == '|' && direction == Direction.TOP -> Continues(direction.applyTo(this))
            token == '|' && direction == Direction.BOTTOM -> Continues(direction.applyTo(this))
            token == '/' && direction == Direction.RIGHT -> Continues(Direction.TOP.applyTo(this))
            token == '/' && direction == Direction.BOTTOM -> Continues(Direction.LEFT.applyTo(this))
            token == '/' && direction == Direction.TOP -> Continues(Direction.RIGHT.applyTo(this))
            token == '/' && direction == Direction.LEFT -> Continues(Direction.BOTTOM.applyTo(this))
            token == '\\' && direction == Direction.RIGHT -> Continues(Direction.BOTTOM.applyTo(this))
            token == '\\' && direction == Direction.TOP -> Continues(Direction.LEFT.applyTo(this))
            token == '\\' && direction == Direction.BOTTOM -> Continues(Direction.RIGHT.applyTo(this))
            token == '\\' && direction == Direction.LEFT -> Continues(Direction.TOP.applyTo(this))
            token == '-' && (direction == Direction.TOP || direction == Direction.BOTTOM) -> MoveResult.Splits(listOf(Direction.LEFT.applyTo(this), Direction.RIGHT.applyTo(this)))
            token == '|' && (direction == Direction.LEFT || direction == Direction.RIGHT) -> MoveResult.Splits(listOf(Direction.BOTTOM.applyTo(this), Direction.TOP.applyTo(this)))
            else -> error("")
        }
    }
}

private fun solve(startingBeam: Beam, map: Array<CharArray>): Int {
    val activeBeams = mutableListOf<Beam>()
    activeBeams.add(startingBeam)
    val beamTraces = mutableSetOf<Beam>()
    while (activeBeams.isNotEmpty()) {
        val beam = activeBeams.removeFirst()
        when(val moveResult = beam.move(map, beamTraces)) {
            is Continues -> {
                beamTraces.add(beam)
                activeBeams.add(moveResult.beam)
            }
            is MoveResult.Splits -> {
                beamTraces.add(beam)
                activeBeams.addAll(moveResult.beams)
            }
            MoveResult.Faded -> {}
        }
    }
    return beamTraces.map { it.x to it.y }.toSet().size
}

private fun solvePartOne() {
    val map = readInputMap()
    println(solve(Beam(0, 0, Direction.RIGHT), map))
}

private fun solvePartTwo() {
    val map = readInputMap()
    val startingBeams = (0 ..< map[0].size).map { Beam(it, 0, Direction.BOTTOM) } +
        (0 ..< map[0].size).map { Beam(it, map.size - 1, Direction.TOP) } +
        map.indices.map { Beam(0, it, Direction.RIGHT) } +
        map.indices.map { Beam(map[0].size - 1, it, Direction.LEFT) }
    var max = 0
    for (startingBeam in startingBeams) {
        max = max(max, solve(startingBeam, map))
    }
    println(max)
}

fun main() {
    solvePartOne()
    solvePartTwo()
}