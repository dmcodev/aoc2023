import java.nio.charset.Charset

fun readInputLines(fileName: String = "input"): List<String> {
    val callerPackage = Throwable().stackTrace.map { it.className }.first { it.startsWith("day") }.substringBefore('.')
    return (object {}).javaClass.getResourceAsStream("/$callerPackage/$fileName")
        .use {
            it.readAllBytes()
                .toString(Charset.defaultCharset())
                .lines()
        }
}

fun readInputMap(fileName: String = "input"): Array<CharArray> =
    readInputLines(fileName).map { it.toCharArray() }.toTypedArray()

fun adjacentPoints(x: Int, y: Int, map: Array<CharArray>): Set<Pair<Int, Int>> =
    sequenceOf(x - 1 to y, x - 1 to y - 1, x to y - 1, x + 1 to y - 1, x + 1 to y, x + 1 to y + 1, x to y + 1, x - 1 to y + 1)
        .filter { it.first >= 0 && it.first < map[0].size && it.second >= 0 && it.second < map.size }
        .toSet()
