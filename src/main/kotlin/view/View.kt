package view

import model.BenchmarkResult
import model.VisualisationType

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

class ViewImpl(override val benchmarkResult: BenchmarkResult) : View {
    override fun render() {
        benchmarkResult.forEach { result ->
            when (result.visualisationType) {
                VisualisationType.SINGLE_VALUE -> println(result.description + result.value[0])
                VisualisationType.LIST_OF_VALUES -> println(result.description + result.value)
            }
        }
    }
}
