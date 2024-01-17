package model

import view.VisualisationType

/**
 * A ScenarioOutput is a data structure that contains the output of a scenario.
 * The key are the name of the values, the values are the list of values.
 *
 * Example:
 * "time frames" -> [1, 2, 3, 4, 5]
 * "distanceTo" -> [20, 15, 14, 13, 13]
 */
typealias ScenarioOutput = Map<String, List<Any>>

/**
 * A BenchmarkOutput is a data structure that contains the output of the benchmark.
 * It maps each scenario to its output.
 *
 * Example:
 * "NetLogo-Tutorial-1" -> ScenarioOutput
 * "NetLogo-Tutorial-2" -> ScenarioOutput
 * "Alchemist-Protelis-1" -> ScenarioOutput
 */
typealias BenchmarkOutput = Map<String, ScenarioOutput>

/**
 * A ScenarioResult is a data structure that contains the result of a scenario.
 * The result means the processed output of a scenario.
 * @param description the description of the result
 * @param value the value of the result
 * @param visualisationType the visualisation type of the result
 *
 */
data class ScenarioResult(
    val description: String,
    val value: List<Any>,
    val visualisationType: VisualisationType,
)

/**
 * A BenchmarkResult is a data structure that contains the result of a benchmark.
 * It is a list of ScenarioResult.
 */
typealias BenchmarkResult = List<ScenarioResult>
