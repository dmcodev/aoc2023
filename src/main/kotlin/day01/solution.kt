package day01

import readInputLines

fun main() {
    readInputLines().asSequence()
        .map(String::extractNumber)
        .sum()
        .apply(::println)
    readInputLines().asSequence()
        .map(String::extractNumberWithTokenization)
        .sum()
        .apply(::println)
}

private val numberTokensMapping = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9) +
    ((1 .. 9).map { it.toString() to it })
private val numberTokens = numberTokensMapping.keys

private fun String.extractNumber(): Int =
    toCharArray().let { chars -> (chars.first { it.isDigit() } + "" + chars.last { it.isDigit() }).toInt() }

private fun String.extractNumberWithTokenization(): Int =
    sequenceOf(findAnyOf(numberTokens), findLastAnyOf(numberTokens))
        .map { it!!.second }
        .map { numberTokensMapping.getValue(it) }
        .joinToString("")
        .toInt()
