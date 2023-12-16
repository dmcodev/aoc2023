package day15

import readInputLines

private data class Box(val lenses: MutableList<Lense>) {

    fun addLense(label: String, length: Int) {
        val existingLenseIndex = lenses.indexOfFirst { it.label == label }
        val lense = Lense(label, length)
        if (existingLenseIndex >= 0) {
            lenses[existingLenseIndex] = lense
        } else {
            lenses.add(lense)
        }
    }

    fun removeLense(label: String) {
        lenses.removeIf { it.label == label }
    }

    fun focusingPower(boxIndex: Int): Long =
        lenses.mapIndexed { slotIndex, lense -> lense.focusingPower(boxIndex, slotIndex + 1) }.sum()
}

private data class Lense(val label: String, val length: Int) {

    fun focusingPower(boxIndex: Int, slotNumber: Int): Long =
        (boxIndex + 1L) * slotNumber * length
}

private fun hash(string: String): Int {
    var result = 0
    string.forEach {
        result += it.code
        result *= 17
        result %= 256
    }
    return result
}

fun main() {
    val input = readInputLines()[0]
    println(input.split(",").sumOf { hash(it) })
    val boxes = mutableListOf<Box>()
    (0 .. 255).forEach { boxes.add(Box(mutableListOf())) }
    input.split(",").forEach {
        if (it.contains("=")) {
            val (label, length) = it.split("=").let { it[0] to it[1].toInt() }
            boxes[hash(label)].addLense(label, length)
        } else if (it.endsWith("-")) {
            val label = it.substringBefore("-")
            boxes[hash(label)].removeLense(label)
        }
    }
    println(boxes.mapIndexed { boxIndex, box -> box.focusingPower(boxIndex) }.sum())
}