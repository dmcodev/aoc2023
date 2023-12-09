package day08

import readInputLines
import java.math.BigInteger

private fun instructionsIterator(instructions: CharArray): Iterator<Pair<Char, Int>> {
    var index = 0
    val sequence = generateSequence {
        if (index >= instructions.size) {
            index = 0
        }
        instructions[index] to index++
    }
    return sequence.iterator()
}

private fun findLCM(a: BigInteger, b: BigInteger): BigInteger {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == BigInteger.ZERO && lcm % b == BigInteger.ZERO) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

private fun findLCMOfListOfNumbers(numbers: List<BigInteger>): BigInteger {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}

private fun partOne() {
    val lines = readInputLines()
    val instructions = lines[0].toCharArray()
    val nodes = mutableMapOf<String, Pair<String, String>>()
    lines.drop(2).forEach { line ->
        nodes[line.substringBefore(" =")] = line.substringAfter("(").substringBefore(")").split(", ").let { it[0] to it[1] }
    }
    var steps = 0
    var label = "AAA"
    val instructionIterator = instructionsIterator(instructions)
    while (label != "ZZZ") {
        label = when(instructionIterator.next().first) {
            'L' -> nodes.getValue(label).first
            'R' -> nodes.getValue(label).second
            else -> error("")
        }
        steps++
    }
    println(steps)
}

private fun partTwo() {
    val lines = readInputLines()
    val instructions = lines[0].toCharArray()
    val nodes = mutableMapOf<String, Pair<String, String>>()
    lines.drop(2).forEach { line ->
        nodes[line.substringBefore(" =")] = line.substringAfter("(").substringBefore(")").split(", ").let { it[0] to it[1] }
    }
    val startingLabels = nodes.keys.filter { it.endsWith("A") }
    val instructionIterator = instructionsIterator(instructions)
    val cycleSteps = mutableSetOf<Long>()
    for (startingLabel in startingLabels) {
        val moments = mutableSetOf<Triple<String, Char, Int>>()
        val targetSteps = mutableSetOf<Int>()
        var label = startingLabel
        var steps = 0
        while (true) {
            val instruction = instructionIterator.next()

            label = when(instruction.first) {
                'L' -> nodes.getValue(label).first
                'R' -> nodes.getValue(label).second
                else -> error("")
            }
            steps++
            if (label[2] == 'Z') {
                val moment = Triple(label, instruction.first, instruction.second)
                if (moments.contains(moment)) {
                    break
                }
                moments.add(moment)
                targetSteps.add(steps)
            }
        }
        if (targetSteps.size != 1) {
            error("")
        }
        cycleSteps.add(targetSteps.min().toLong())
    }
    println(findLCMOfListOfNumbers(cycleSteps.map { BigInteger.valueOf(it) }))
}

fun main() {
    partOne()
    partTwo()
}
