package day12

import readInputLines

private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

private fun compute(chars: String, position: Int, numbers: List<Int>): Long {
    if (numbers.isEmpty()) {
        if (position <= chars.length && chars.substring(position, chars.length).any { it == '#' }) {
            return 0
        }
        return 1
    }
    if (position >= chars.length) {
        return 0
    }
    val cacheKey = chars.substring(position, chars.length) to numbers
    if (cache.containsKey(cacheKey)) {
        return cache.getValue(cacheKey)
    }
    val nextNumbers = numbers.drop(1)
    val requiredSpaceUpfront = numbers.sum() + (numbers.size - 1)
    val candidateRangeEnd = chars.length - requiredSpaceUpfront
    var result = 0L
    for (i in position .. candidateRangeEnd) {
        if (i > 0 && chars[i - 1] == '#') {
            break
        }
        val tokenEnd = i + numbers.first()
        if (tokenEnd < chars.length && chars[tokenEnd] == '#') {
            continue
        }
        val token = chars.substring(i, tokenEnd)
        if (!token.all { it == '?' || it == '#' }) {
            continue
        }
        result += compute(chars, tokenEnd + 1, nextNumbers)
    }
    cache[cacheKey] = result
    return result
}

private fun solve(multiplier: Int = 1) {
    println(
        readInputLines().sumOf { line ->
            val chars = generateSequence { line.split(" ")[0] }.take(multiplier).joinToString(separator = "?")
            val numbers = generateSequence { line.split(" ")[1].split(",").map { it.toInt() } }.take(multiplier).flatten().toList()
            compute(chars, 0, numbers)
        }
    )
}

fun main() {
    solve(1)
    solve(5)
}