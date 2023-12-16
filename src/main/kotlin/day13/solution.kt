package day13

import readInputLines
import kotlin.math.min

private fun readPatterns(): List<List<String>> {
    val patterns = mutableListOf<List<String>>()
    var currentPattern = mutableListOf<String>()
    patterns.add(currentPattern)
    readInputLines().forEach { line ->
        if (line.isBlank()) {
            currentPattern = mutableListOf()
            patterns.add(currentPattern)
        } else {
            currentPattern.add(line)
        }
    }
    return patterns
}

private fun smudged(left: String, right: String): Boolean =
    left.indices.count { left[it] - right[it] != 0 } == 1

private fun findHorizontalReflection(pattern: List<String>): Int {
    for (y in 1 ..< pattern.size) {
        val top = pattern.subList(0, y).reversed()
        val bottom = pattern.subList(y, pattern.size)
        val checkSize = min(top.size, bottom.size)
        if ((0 ..< checkSize).all { top[it] == bottom[it] }) {
            return top.size
        }
    }
    return 0
}

private fun findHorizontalReflectionSmudged(pattern: List<String>, excludedY: Int = -1): Int {
    outer@ for (y in 1 ..< pattern.size) {
        if (y == excludedY) {
            continue
        }
        val top = pattern.subList(0, y).reversed()
        val bottom = pattern.subList(y, pattern.size)
        val checkSize = min(top.size, bottom.size)
        var smudged = false
        for (i in 0 ..< checkSize) {
            if (top[i] != bottom[i]) {
                if (smudged) {
                    continue@outer
                }
                if (smudged(top[i], bottom[i])) {
                    smudged = true
                } else {
                    continue@outer
                }
            }
        }
        if (smudged) {
            return top.size
        }
    }
    return 0
}

private fun findVerticalReflection(pattern: List<String>): Int {
    for (x in 1 ..< pattern[0].length) {
        val left = (0..< x).map { i -> pattern.map { it[i] }.joinToString("") }.reversed()
        val right = (x..< pattern[0].length).map { i -> pattern.map { it[i] }.joinToString("") }
        val checkSize = min(left.size, right.size)
        if ((0 ..< checkSize).all { left[it] == right[it] }) {
            return left.size
        }
    }
    return 0
}

private fun findVerticalReflectionSmudged(pattern: List<String>, excludedX: Int = -1): Int {
    outer@ for (x in 1 ..< pattern[0].length) {
        if (x == excludedX) {
            continue
        }
        val left = (0..< x).map { i -> pattern.map { it[i] }.joinToString("") }.reversed()
        val right = (x..< pattern[0].length).map { i -> pattern.map { it[i] }.joinToString("") }
        val checkSize = min(left.size, right.size)
        var smudged = false
        for (i in 0 ..< checkSize) {
            if (left[i] != right[i]) {
                if (smudged) {
                    continue@outer
                }
                if (smudged(left[i], right[i])) {
                    smudged = true
                } else {
                    continue@outer
                }
            }
        }
        if (smudged) {
            return left.size
        }
    }
    return 0
}

private fun solve(): Long {
    val patterns = readPatterns()
    var result = 0L
    for (pattern in patterns) {
        val horizontalReflection = findHorizontalReflection(pattern)
        result += if (horizontalReflection > 0) horizontalReflection * 100 else findVerticalReflection(pattern)
    }
    return result
}

private fun solveSmudged(): Long {
    val patterns = readPatterns()
    var result = 0L
    for (pattern in patterns) {
        val horizontalReflection = findHorizontalReflection(pattern)
        val horizontalReflectionSmudged = findHorizontalReflectionSmudged(pattern, horizontalReflection)
        val verticalReflection = findVerticalReflection(pattern)
        val verticalReflectionSmudged = findVerticalReflectionSmudged(pattern, verticalReflection)
        result += horizontalReflectionSmudged * 100 + verticalReflectionSmudged

    }
    return result
}

fun main() {
    println(solve())
    println(solveSmudged())
}