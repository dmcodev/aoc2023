package day06

import readInputLines
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

private fun optimize(t: Double, d: Double): Int {
    val delta = sqrt(t * t - 4 * d)
    val tms = listOf((-t - delta) / -2, (-t + delta) / -2).sorted()
    val start = ceil(tms[0]).let { if (it == tms[0]) it + 1 else it }.toInt()
    val end = floor(tms[1]).let { if (it == tms[1]) it - 1 else it }.toInt()
    return (start .. end).count()
}

fun main() {
    val lines = readInputLines()
    val times = lines[0].substringAfter(":").split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toDouble() }
    val distance = lines[1].substringAfter(":").split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toDouble() }
    var result = 1
    for (i in times.indices) {
        result *= optimize(times[i], distance[i])
    }
    println(result)
    val longTime = lines[0].replace(Regex("\\s+"), "").substringAfter(":").toDouble()
    val longDistance = lines[1].replace(Regex("\\s+"), "").substringAfter(":").toDouble()
    println(optimize(longTime, longDistance))
}
