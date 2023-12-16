package day14

import readInputMap

private fun tilt(rail: String, inverted: Boolean = false): String {
    var separatorsCount = rail.count { it == '#' }
    val builder = StringBuilder()
    rail.split("#").forEach { part ->
        val count = part.count { it == 'O' }
        if (!inverted) {
            builder.append("O".repeat(count)).append(".".repeat(part.length - count))
        } else {
            builder.append(".".repeat(part.length - count)).append("O".repeat(count))
        }
        if (separatorsCount-- > 0) {
            builder.append("#")
        }
    }
    return builder.toString()
}

private fun tiltNorth(map: List<String>): List<String> {
    val rails = map[0].indices.map { i -> map.map { it[i] }.joinToString("") }
    val mappedRails = rails.map { tilt(it) }
    return map.indices.map { i -> mappedRails.map { it[i] }.joinToString("") }
}

private fun tiltWest(map: List<String>): List<String> =
    map.map { tilt(it) }

private fun tiltSouth(map: List<String>): List<String> {
    val rails = map[0].indices.map { i -> map.map { it[i] }.joinToString("") }
    val mappedRails = rails.map { tilt(it, true) }
    return map.indices.map { i -> mappedRails.map { it[i] }.joinToString("") }
}

private fun tiltEast(map: List<String>): List<String> =
    map.map { tilt(it, true) }

private fun cycle(map: List<String>): List<String> {
    return tiltEast(tiltSouth(tiltWest(tiltNorth(map))))
}

private fun partOne() {
    val inputMap = readInputMap().map { it.joinToString("") }
    val tiltedMap = tiltNorth(inputMap)
    println(tiltedMap.reversed().mapIndexed { i, row -> (i + 1) * row.count { it == 'O' } }.sum())
}

private fun partTwo() {
    val inputMap = readInputMap().map { it.joinToString("") }
    var tiltedMap = inputMap
    val mapCodes = mutableSetOf<Int>()
    val mapCodeToCycleCompleted = mutableMapOf<Int, Int>()
    var repeatingCyclesCompletedStart = -1
    var repeatingCyclesCompletedEnd = -1
    var cyclesCompleted = 0
    while (cyclesCompleted < 1000000000) {
        val mapCode = tiltedMap.joinToString("").hashCode()
        if (mapCodes.contains(mapCode)) {
            repeatingCyclesCompletedStart = mapCodeToCycleCompleted.getValue(mapCode)
            repeatingCyclesCompletedEnd = cyclesCompleted
            break
        }
        mapCodes.add(mapCode)
        mapCodeToCycleCompleted[mapCode] = cyclesCompleted
        tiltedMap = cycle(tiltedMap)
        cyclesCompleted++
    }
    val repeatingCycleSize = repeatingCyclesCompletedEnd - repeatingCyclesCompletedStart
    val cyclesToComplete = 1000000000 - cyclesCompleted
    val cyclesToCompleteSlow = cyclesToComplete % repeatingCycleSize
    repeat(cyclesToCompleteSlow) {
        tiltedMap = cycle(tiltedMap)
    }
    println(tiltedMap.reversed().mapIndexed { i, row -> (i + 1) * row.count { it == 'O' } }.sum())
}

fun main() {
    partOne()
    partTwo()
}



