package view

import model.BenchmarkResult

/**
 * The visualisation type defines how the output of a benchmark is visualised.
 * SINGLE_VALUE the output is a single value
 * LIST_OF_VALUES the output is a list of values
 */
enum class VisualisationType {
    SINGLE_VALUE,
    LIST_OF_VALUES,
}

/**
 * An interface to represent the benchmark result.
 */
interface View {
    /**
     * The benchmark result.
     */
    val benchmarkResult: BenchmarkResult

    /**
     * Render the benchmark result.
     */
    fun render()
}

/**
 * A basic implementation of the View interface that simply prints the benchmark result to the console.
 */
class ViewImpl(override val benchmarkResult: BenchmarkResult) : View {
    override fun render() {
        println("[TESTBED] Results:")
        benchmarkResult.forEach { result ->
            when (result.visualisationType) {
                VisualisationType.SINGLE_VALUE -> println(result.description + result.value[0])
                VisualisationType.LIST_OF_VALUES -> println(result.description + result.value)
            }
        }
    }
}
