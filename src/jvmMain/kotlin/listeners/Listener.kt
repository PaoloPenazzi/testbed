package reader

typealias Metric = Map<String, List<Any>>

interface Listener {
    fun readCsv(path: String): Metric
}