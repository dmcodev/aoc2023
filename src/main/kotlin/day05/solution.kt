package day05

import readInputLines
import kotlin.math.max
import kotlin.math.min

private data class Mapping(val from: LongRange, val to: LongRange) {
    val shift = to.first - from.first
}

private fun parseMappings(lines: List<String>): List<List<Mapping>> {
    val mappings = mutableListOf<MutableList<Mapping>>()
    var currentMappings = mutableListOf<Mapping>()
    lines.asSequence().drop(2).forEach { line ->
        if (line.isNotEmpty()) {
            if (line[0].isLetter()) {
                currentMappings = mutableListOf()
                mappings.add(currentMappings)
            } else if (line[0].isDigit()) {
                val mapping = line.split(" ").map { it.toLong() }
                currentMappings.add(Mapping(mapping[1] ..< mapping[1] + mapping[2], mapping[0] ..< mapping[0] + mapping[2]))
            }
        }
    }
    return mappings
}

private fun aggregateMappings(first: List<Mapping>, second: List<Mapping>, addInner: Boolean = true): List<Mapping> {
    val outerMutable = mutableListOf<Mapping>().apply { addAll(first) }
    val innerMutable = mutableListOf<Mapping>().apply { addAll(second) }
    val result = mutableListOf<Mapping>()
    outer@ do {
        var changes = false
        for (outer in outerMutable) {
            for (inner in innerMutable) {
                if (inner.from.first <= outer.to.last && inner.from.last >= outer.to.first) {
                    outerMutable.remove(outer)
                    innerMutable.remove(inner)
                    val common = max(inner.from.first, outer.to.first) .. min(inner.from.last, outer.to.last)
                    if (common.first > outer.to.first) {
                        outerMutable.add(
                            Mapping(
                                outer.from.first.. outer.from.last - (outer.to.last - common.first + 1),
                                outer.to.first ..< common.first
                            )
                        )
                    }
                    if (common.last < outer.to.last) {
                        outerMutable.add(
                            Mapping(
                                outer.from.last - (outer.to.last - common.last) + 1 .. outer.from.last,
                                common.last + 1 .. outer.to.last
                            )
                        )
                    }
                    if (common.first > inner.from.first) {
                        innerMutable.add(
                            Mapping(
                                inner.from.first ..< common.first,
                                inner.to.first ..< inner.to.first + common.first - inner.from.first
                            )
                        )
                    }
                    if (common.last < inner.from.last) {
                        innerMutable.add(
                            Mapping(
                                common.last + 1 .. inner.from.last,
                                inner.to.last - (inner.from.last - common.last - 1) .. inner.to.last
                            )
                        )
                    }
                    result.add(
                        Mapping(
                            common.first - outer.shift .. common.last - outer.shift,
                            common.first + inner.shift .. common.last + inner.shift
                        )
                    )
                    changes = true
                    continue@outer
                }
            }
        }
    } while (changes)
    result.addAll(outerMutable)
    if (addInner) {
        result.addAll(innerMutable)
    }
    return result
}

private fun mapNumber(number: Long, mappings: List<Mapping>): Long =
    mappings.firstOrNull { it.from.first <= number && it.from.last >= number }
        ?.let { number + it.shift }
        ?: number

fun main() {
    val lines = readInputLines()
    val seedNumbers = lines.first().substringAfter(":").splitToSequence(" ")
        .map { it.trim() }.filter { it.isNotEmpty() }.map { it.toLong() }.toSet()
    val mappings = parseMappings(lines)
    var aggregatedMapping = mappings[0].toList()
    mappings.drop(1).forEach { aggregatedMapping = aggregateMappings(aggregatedMapping, it) }
    println(seedNumbers.minOf { mapNumber(it, aggregatedMapping) })
    val seedNumbersMappings = seedNumbers.windowed(2, 2)
        .map { it[0] ..< it[0] + it[1] }
        .map { Mapping(it, it) }
    val extendedAggregatedMapping = aggregateMappings(seedNumbersMappings, aggregatedMapping, false)
    println(extendedAggregatedMapping.minOfOrNull { it.to.first })
}
